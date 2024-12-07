import { createRouter, createWebHistory } from 'vue-router';
import WaterfallBasicKnowledge from '@/views/WaterfallBasicKnowledge.vue';
import WaterfallModelFramework from '@/views/WaterfallModelFramework.vue';
import WaterfallSimulationTest from '@/views/WaterfallSimulationTest.vue';
import login from '@/views/login.vue';

const routes = [
  { path: '/waterfall-basic-knowledge', component: WaterfallBasicKnowledge, meta: { title: '基础知识' } },
  { path: '/waterfall-model-framework', component: WaterfallModelFramework, meta: { title: '知识框架' } },
  { path: '/waterfall-simulation-test', component: WaterfallSimulationTest, meta: { title: '模拟测试' } },
  { path: '/login', component: login, meta: { title: '登录', hideNavBar: true } }, // 添加 hideNavBar: true
  { path: '/', redirect: '/login' } // 默认页面
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
