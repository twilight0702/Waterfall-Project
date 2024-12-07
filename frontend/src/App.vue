<template>
  <div id="app">
    <!-- 根据路由 meta.hideNavBar 属性动态显示或隐藏 NavBar -->
    <NavBar />
    <router-view />
  </div>
</template>

<script>
import NavBar from './components/NavBar.vue';

export default {
  name: 'App',
  components: {
    NavBar,
  },
};
</script>

<style>
/* 去除 #app 的 padding */
#app {
  padding: 0;
}
</style>
