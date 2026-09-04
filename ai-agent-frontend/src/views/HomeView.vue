<script setup>
import { ArrowRight, Bot, MessageSquareCode, MessageSquareText } from '@lucide/vue';

const apps = [
  {
    name: '智码编程助手',
    description: '陪你梳理需求、理解报错、完善思路，让卡住的编程问题更快找到方向。',
    route: '/love-master',
    tone: 'love',
    Icon: MessageSquareCode,
    meta: '编程助手',
  },
  {
    name: 'AI 超级智能体',
    description: '把复杂目标交给智能体推进，从任务拆解到结果整理，帮你稳步完成更多事情。',
    route: '/super-agent',
    tone: 'agent',
    Icon: Bot,
    meta: '任务智能体',
  },
];
</script>

<template>
  <main class="home-page">
    <section class="home-hero">
      <p class="eyebrow">AI Application Console</p>
      <h1>选择一个 AI 应用开始对话</h1>
      <p>
        选择适合当前任务的 AI 助手，让每一次提问都得到清晰、及时的回应。
      </p>
    </section>

    <section class="app-grid" aria-label="应用列表">
      <RouterLink
        v-for="app in apps"
        :key="app.route"
        class="app-card"
        :class="`app-card--${app.tone}`"
        :to="app.route"
      >
        <div class="app-card__icon">
          <component :is="app.Icon" :size="30" />
        </div>
        <div class="app-card__body">
          <span>{{ app.meta }}</span>
          <h2>{{ app.name }}</h2>
          <p>{{ app.description }}</p>
        </div>
        <div class="app-card__action">
          <MessageSquareText :size="18" />
          <span>进入应用</span>
          <ArrowRight :size="18" />
        </div>
      </RouterLink>
    </section>
  </main>
</template>
