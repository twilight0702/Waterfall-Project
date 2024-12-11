import { createRouter, createWebHistory } from 'vue-router';
import WaterfallBasicKnowledge from '@/views/WaterfallBasicKnowledge.vue';
import WaterfallModelFramework from '@/views/WaterfallModelFramework.vue';
import WaterfallSimulationTest from '@/views/WaterfallSimulationTest.vue';
import login from '@/views/login.vue';
import CompletedExercises from '@/views/CompletedExercises.vue'; // 导入已做习题页面
import WrongQuestions from '@/views/WrongQuestions.vue'; // 导入错题集页面
import GradeRecord from '@/views/GradeRecord.vue'; // 导入成绩记录页面

const routes = [
  { path: '/waterfall-basic-knowledge', component: WaterfallBasicKnowledge, meta: { title: '基础知识' } },
  { path: '/waterfall-model-framework', component: WaterfallModelFramework, meta: { title: '知识框架' } },
  { path: '/waterfall-simulation-test', component: WaterfallSimulationTest, meta: { title: '模拟测试' } },
  { path: '/login', component: login, meta: { title: '登录' } }, 
  { path: '/completed-exercises', component: CompletedExercises, meta: { title: '已做习题' } },  // 新增已做习题路由
  { path: '/wrong-questions', component: WrongQuestions, meta: { title: '错题集' } }, // 新增错题集路由
  { path: '/grade-record', component: GradeRecord, meta: { title: '成绩记录' } }, // 新增成绩记录路由
  { path: '/', redirect: '/login' } // 默认页面
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
