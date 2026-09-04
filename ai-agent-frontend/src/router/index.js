import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import LoveMasterView from '../views/LoveMasterView.vue';
import SuperAgentView from '../views/SuperAgentView.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/love-master',
      name: 'love-master',
      component: LoveMasterView,
    },
    {
      path: '/super-agent',
      name: 'super-agent',
      component: SuperAgentView,
    },
  ],
});

export default router;
