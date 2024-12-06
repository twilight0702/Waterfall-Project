<!-- NavBar.vue -->
<template>
  <nav>
    <div class="logo"></div>
    <ul>
      <li><router-link to="/waterfall-basic-knowledge">基础知识</router-link></li>
      <li><router-link to="/waterfall-model-framework">知识框架</router-link></li>
      <li><router-link to="/waterfall-application">应用</router-link></li>
      <li><router-link to="/waterfall-simulation-test">模拟测试</router-link></li>
    </ul>
  </nav>
</template>

<script>
export default {
  name: 'NavBar'
};
</script>

<style scoped>
nav {
  position: fixed;
  top: 0;
  left: 0;
  background-color: var(--color-background-soft);
  padding: 10px;
  width: 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 999;
  display: flex;
  align-items: center;
}

ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
  display: flex;
  justify-content: space-around;
  align-items: center;
  flex-wrap: wrap;
  width: 100%;
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
