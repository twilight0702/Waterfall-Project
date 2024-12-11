<template>
  <nav :style="{ visibility: navbarHidden ? 'hidden' : 'visible' }">
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
    <div :class="['sidebar', { show: sidebarVisible }]">
      <div class="close-btn" @click="toggleSidebar">×</div> <!-- 关闭按钮 -->
      <ul>
        <li @click="goToCompletedExercises">已做习题</li>
        <li @click="goToWrongQuestions">错题集</li>
        <li @click="goToGradeRecord">成绩记录</li> <!-- 新增的成绩记录菜单项 -->
      </ul>
    </div>
  </nav>
</template>

<script>
import eventBus from '@/eventBus'

export default {
  name: 'NavBar',
  data() {
    return {
      username: '', // 用来存储登录后的用户名
      sidebarVisible: false, // 控制侧边栏的显示与隐藏
    };
  },
  created() {
    this.navbarHidden = 1;
    if (!this.username) this.username = "未登录";
    eventBus.on('usernameUpdated', (newUsername) => {
      if (newUsername) this.username = newUsername;
      this.navbarHidden = 0;
    });
  },
  beforeDestroy() {
    eventBus.off('usernameUpdated');
  },
  methods: {
    // 控制侧边栏的显示与隐藏
    toggleSidebar() {
      console.log('点击了用户名');  // 用于调试，检查点击事件是否触发
      this.sidebarVisible = !this.sidebarVisible;  // 切换侧边栏的显示状态
    },
    // 跳转到已做习题页面
    goToCompletedExercises() {
      this.$router.push('/completed-exercises');
    },
    // 跳转到错题集页面
    goToWrongQuestions() {
      this.$router.push('/wrong-questions');
    },
    // 跳转到成绩记录页面
    goToGradeRecord() {
      this.$router.push('/grade-record');
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
  height: 50px;
  background-color: #fff;
  padding: 0 20px;
  width: 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 999;
  display: flex;
  justify-content: space-between;
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
  width: 70%;
}

li {
  margin: 0 10px;
}

a {
  text-decoration: none;
  font-weight: bold;
  padding: 10px 15px;
  border-radius: 5px;
  color: #333;
  transition: all 0.3s ease-in-out;
}

a:hover {
  background-color: #e0f7fa;
  color: #00796b;
}

.router-link-active {
  background-color: #00796b;
  color: #fff;
}

/* 用户名区域 */
.user-info {
  font-size: 16px;
  color: #00796b;
  cursor: pointer;
  padding: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 1%;
  transition: background-color 0.3s ease-in-out;
  border-radius: 5px;
}

.user-info:hover {
  background-color: #e0f7fa;
}

/* 侧边栏样式 */
.sidebar {
  position: fixed;
  top: 60px; /* 保持与导航栏的距离 */
  right: 0;
  background: #ffffff;
  width: 240px;
  height: 100vh;
  box-shadow: -4px 0 15px rgba(0, 0, 0, 0.1); /* 更柔和的阴影 */
  padding: 20px;
  z-index: 9999;
  transition: transform 0.3s ease-in-out;
  transform: translateX(100%); /* 初始状态右侧隐藏 */
  border-radius: 10px 0 0 10px; /* 圆角效果 */
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.sidebar.show {
  transform: translateX(0); /* 侧边栏弹出 */
}

.sidebar .close-btn {
  position: absolute;
  top: 20px;
  left: 20px;
  font-size: 24px;
  cursor: pointer;
  color: #00796b;
  transition: color 0.3s ease;
}

.sidebar .close-btn:hover {
  color: #d32f2f;
}

/* 侧边栏菜单项 */
.sidebar ul {
  list-style-type: none;
  padding: 0;
  margin: 0;

}

.sidebar li {
  cursor: pointer;
  font-size: 17px;
  font-weight: normal;
  margin-top: 15px;
  padding: 12px 20px;
  transition: background-color 0.3s ease, color 0.3s ease;
  border-radius: 8px;
  color: #555;
}

.sidebar li:hover {
  background-color: #00796b;
  color: white;
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
