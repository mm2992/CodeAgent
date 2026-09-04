<script setup>
import { onMounted, ref } from 'vue';
import ChatRoom from '../components/ChatRoom.vue';

const chatId = ref('');

function createChatId() {
  if (globalThis.crypto?.randomUUID) {
    return `zhima-${globalThis.crypto.randomUUID()}`;
  }

  return `zhima-${Date.now()}-${Math.random().toString(16).slice(2)}`;
}

onMounted(() => {
  chatId.value = createChatId();
});
</script>

<template>
  <ChatRoom
    v-if="chatId"
    title="智码编程助手"
    subtitle="把需求、报错、代码片段和实现思路说清楚，让 AI 帮你快速梳理解决方案。"
    placeholder="描述你的编程问题，或直接粘贴代码、报错信息..."
    mode="love"
    :chat-id="chatId"
  />
</template>
