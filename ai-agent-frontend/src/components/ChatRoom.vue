<script setup>
import { computed, nextTick, onBeforeUnmount, reactive, ref } from 'vue';
import { ArrowLeft, Bot, Loader2, MessageSquareCode, Send, UserRound } from '@lucide/vue';
import { streamLoveChat, streamManusChat } from '../services/chatStream';

const props = defineProps({
  title: {
    type: String,
    required: true,
  },
  subtitle: {
    type: String,
    required: true,
  },
  placeholder: {
    type: String,
    required: true,
  },
  mode: {
    type: String,
    required: true,
    validator: (value) => ['love', 'agent'].includes(value),
  },
  chatId: {
    type: String,
    default: '',
  },
});

const messages = ref([]);
const inputText = ref('');
const isStreaming = ref(false);
const errorText = ref('');
const messageList = ref(null);
const composerInput = ref(null);
const TYPEWRITER_DELAY_MS = 25;
let cancelActiveStream = null;
let typingTimer = null;
let typingQueue = [];
let streamEnded = false;
let activeReply = null;
let activeRequestId = 0;

const canSend = computed(() => inputText.value.trim().length > 0 && !isStreaming.value);
const modeLabel = computed(() => (props.mode === 'love' ? 'Code Session' : 'Agent Session'));
const assistantIcon = computed(() => (props.mode === 'love' ? MessageSquareCode : Bot));

function createMessageId() {
  if (globalThis.crypto?.randomUUID) {
    return globalThis.crypto.randomUUID();
  }

  return `${Date.now()}-${Math.random().toString(16).slice(2)}`;
}

function scrollToBottom() {
  nextTick(() => {
    if (!messageList.value) return;
    messageList.value.scrollTop = messageList.value.scrollHeight;
  });
}

function resetComposerHeight() {
  nextTick(() => {
    if (!composerInput.value) return;
    composerInput.value.style.height = 'auto';
    composerInput.value.style.height = `${Math.min(composerInput.value.scrollHeight, 132)}px`;
  });
}

function clearTypingTimer() {
  if (typingTimer === null) return;
  clearTimeout(typingTimer);
  typingTimer = null;
}

function finishStream(reply, requestId) {
  if (requestId !== activeRequestId) return;

  clearTypingTimer();

  if (!reply.content.trim()) {
    reply.content = '这次没有收到有效回复，请换个说法再试一次。';
  }

  reply.pending = false;
  isStreaming.value = false;
  cancelActiveStream = null;
  typingQueue = [];
  streamEnded = false;
  activeReply = null;
  scrollToBottom();
}

function scheduleNextCharacter(reply, requestId) {
  if (
    typingTimer !== null ||
    typingQueue.length === 0 ||
    requestId !== activeRequestId ||
    activeReply !== reply
  ) {
    return;
  }

  typingTimer = setTimeout(() => {
    typingTimer = null;

    if (requestId !== activeRequestId || activeReply !== reply) {
      return;
    }

    const nextCharacter = typingQueue.shift();

    if (nextCharacter !== undefined) {
      reply.content += nextCharacter;
      scrollToBottom();
    }

    if (typingQueue.length > 0) {
      scheduleNextCharacter(reply, requestId);
    } else if (streamEnded) {
      finishStream(reply, requestId);
    }
  }, TYPEWRITER_DELAY_MS);
}

function enqueueChunk(reply, chunk, requestId) {
  if (requestId !== activeRequestId || activeReply !== reply) return;

  typingQueue.push(...Array.from(chunk));
  scheduleNextCharacter(reply, requestId);
}

function markStreamEnded(reply, requestId) {
  if (requestId !== activeRequestId || activeReply !== reply) return;

  streamEnded = true;

  if (typingQueue.length === 0 && typingTimer === null) {
    finishStream(reply, requestId);
  }
}

function failStream(reply, requestId) {
  if (requestId !== activeRequestId) return;

  clearTypingTimer();
  typingQueue = [];
  streamEnded = false;
  activeReply = null;
  reply.pending = false;
  reply.failed = true;
  reply.content = reply.content || '连接失败了，请确认后端服务已启动，稍后再试。';
  errorText.value = '当前对话连接异常，请检查后端服务或网络状态。';
  isStreaming.value = false;
  cancelActiveStream = null;
  scrollToBottom();
}

function sendMessage() {
  const content = inputText.value.trim();

  if (!content || isStreaming.value) {
    return;
  }

  errorText.value = '';
  inputText.value = '';
  resetComposerHeight();

  messages.value.push({
    id: createMessageId(),
    role: 'user',
    content,
  });

  const reply = reactive({
    id: createMessageId(),
    role: 'assistant',
    content: '',
    pending: true,
    failed: false,
  });

  const requestId = ++activeRequestId;
  clearTypingTimer();
  typingQueue = [];
  streamEnded = false;
  activeReply = reply;

  messages.value.push(reply);
  isStreaming.value = true;
  scrollToBottom();

  const stream = props.mode === 'love' ? streamLoveChat : streamManusChat;
  cancelActiveStream = stream({
    message: content,
    chatId: props.chatId,
    onChunk: (chunk) => {
      enqueueChunk(reply, chunk, requestId);
    },
    onDone: () => markStreamEnded(reply, requestId),
    onError: () => failStream(reply, requestId),
  });
}

function handleInput() {
  resetComposerHeight();
}

onBeforeUnmount(() => {
  activeRequestId += 1;
  cancelActiveStream?.();
  cancelActiveStream = null;
  clearTypingTimer();
  typingQueue = [];
  streamEnded = false;
  activeReply = null;
});
</script>

<template>
  <main class="chat-page" :class="`chat-page--${mode}`">
    <header class="chat-header">
      <RouterLink class="back-link" to="/" aria-label="返回主页">
        <ArrowLeft :size="18" />
        <span>主页</span>
      </RouterLink>

      <section class="chat-heading">
        <div class="agent-mark" aria-hidden="true">
          <component :is="assistantIcon" :size="24" />
        </div>
        <div>
          <p class="session-kicker">{{ modeLabel }}</p>
          <h1>{{ title }}</h1>
          <p>{{ subtitle }}</p>
        </div>
      </section>

      <div v-if="chatId" class="session-chip" :title="chatId">
        <span>Chat ID</span>
        <strong>{{ chatId }}</strong>
      </div>
    </header>

    <section ref="messageList" class="message-list" aria-live="polite">
      <div v-if="messages.length === 0" class="empty-state">
          <p class="empty-label">Ready</p>
          <h2>{{ title }}</h2>
          <p>输入你的需求或问题，AI 会以流式方式把回复实时推送到这里。</p>
      </div>

      <article
        v-for="message in messages"
        :key="message.id"
        class="message-row"
        :class="[`message-row--${message.role}`, { 'message-row--failed': message.failed }]"
      >
        <div class="message-avatar" aria-hidden="true">
          <UserRound v-if="message.role === 'user'" :size="18" />
          <component v-else :is="assistantIcon" :size="18" />
        </div>
        <div class="message-bubble">
          <p>{{ message.content }}</p>
          <span v-if="message.pending" class="streaming-caret">
            <Loader2 :size="14" />
            生成中
          </span>
        </div>
      </article>
    </section>

    <footer class="composer-wrap">
      <p v-if="errorText" class="error-text">{{ errorText }}</p>
      <form class="composer" @submit.prevent="sendMessage">
        <textarea
          ref="composerInput"
          v-model="inputText"
          :placeholder="placeholder"
          :disabled="isStreaming"
          rows="1"
          @input="handleInput"
          @keydown.enter.exact.prevent="sendMessage"
        />
        <button class="send-button" type="submit" :disabled="!canSend" aria-label="发送消息">
          <Loader2 v-if="isStreaming" :size="20" />
          <Send v-else :size="20" />
          <span>发送</span>
        </button>
      </form>
    </footer>
  </main>
</template>
