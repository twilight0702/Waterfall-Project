<template>
  <nav>
    <div class="logo"></div>
    <ul>
      <li><router-link to="/waterfall-basic-knowledge">基础知识</router-link></li>
      <li><router-link to="/waterfall-model-framework">知识框架</router-link></li>
      <li><router-link to="/waterfall-simulation-test">模拟测试</router-link></li>
    </ul>
    <div class="user-info" @click="toggleSidebar">
      <span>{{ username }}</span> <!-- 显示用户名 -->
    </div>

    <!-- 侧边栏 -->
    <div v-if="sidebarVisible" class="sidebar">
      <ul>
        <li @click="goToCompletedExercises">已做习题</li>
        <li @click="goToWrongQuestions">错题集</li>
      </ul>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'NavBar',
  data() {
    return {
      username: '', // 用来存储登录后的用户名
      sidebarVisible: false, // 控制侧边栏的显示与隐藏
    };
  },
  created() {
    // 在组件创建时获取用户名，假设用户名已通过路由状态传递
    this.username = this.$route.state?.username || '未登录';
  },
  methods: {
    // 控制侧边栏的显示与隐藏
    toggleSidebar() {
      console.log('点击了用户名');  // 用于调试，检查点击事件是否触发
      this.sidebarVisible = !this.sidebarVisible;
    },
    // 跳转到已做习题页面
    goToCompletedExercises() {
      this.$router.push('/completed-exercises');
    },
    // 跳转到错题集页面
    goToWrongQuestions() {
      this.$router.push('/wrong-questions');
    }
  }
};
</script>

<style scoped>
/* 导航栏 */
nav {
  position: fixed;
  top: 0;
  left: 0;
  height: 45px;
  background-color: var(--color-background-soft);
  padding: 10px;
  width: 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 999;
  display: flex;
  justify-content: space-between; /* 使左右两边内容分开 */
  align-items: center;
}

/* 导航栏菜单 */
ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
  display: flex;
  justify-content: space-around;
  align-items: center;
  flex-wrap: wrap;
  width: 70%; /* 限制导航栏宽度 */
}

li {
  margin: 0 10px;
}

a {
  text-decoration: none;
  font-weight: bold;
  padding: 10px 15px;
  border-radius: 5px;
  color: var(--color-primary);
  transition: all 0.3s ease-in-out;
}

a:hover {
  background-color: var(--color-primary-light);
  color: rgb(0, 104, 61);
}

.router-link-active {
  background-color: var(--color-primary);
  color: rgb(0, 104, 61);
}

/* 用户名区域 */
.user-info {
  font-size: 15px;
  color: var(--color-primary);
  cursor: pointer;
  padding: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 侧边栏样式 */
.sidebar {
  position: fixed;
  top: 0;
  right: 0;
  background-color: white;
  width: 200px;
  height: 100vh;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.2);
  padding: 20px;
  z-index: 9999;
  transition: transform 0.3s ease-in-out;
  transform: translateX(100%); /* 初始状态右侧隐藏 */
}

.sidebar ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.sidebar li {
  padding: 15px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.sidebar li:hover {
  background-color: #f0f0f0;
}

.sidebar-enter-active, .sidebar-leave-active {
  transition: transform 0.3s ease-in-out;
}

.sidebar-enter, .sidebar-leave-to /* .sidebar-leave-active in <2.1.8 */ {
  transform: translateX(100%);
}

/* 小屏幕时调整导航栏布局 */
@media (max-width: 768px) {
  ul {
    flex-direction: column;
    align-items: flex-start;
  }

  li {
    width: 100%;
    margin: 5px 0;
  }

  a {
    display: block;
    width: 100%;
    text-align: left;
  }
}
</style>
