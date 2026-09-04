<script setup>
import ChatRoom from '../components/ChatRoom.vue';
</script>

<template>
  <ChatRoom
    title="AI 超级智能体"
    subtitle="描述你的目标，智能体会以流式响应帮你拆解任务、推进思路和生成执行建议。"
    placeholder="输入你想让智能体处理的任务..."
    mode="agent"
  />
</template>
