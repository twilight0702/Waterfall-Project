<template>
    <div>
      <h1>登录</h1>
      <div>
        <input v-model="name" placeholder="用户名" />
        <br />
        <input v-model="Password" type="password" placeholder="密码" />
        <br />
        <button @click="login">登录</button>
        <button @click="register">注册</button>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        name: '',
        Password: '',
      };
    },
    methods: {
      // 登录逻辑
      async login() {
        const { name, Password } = this;
  
        if (!name || !Password) {
          alert('用户名和密码不能为空');
          return;
        }
  
        try {
            console.log('用户名:', name);
            console.log('密码:', Password);
          const response = await axios.post('http://localhost:3000/login', {
            username: name,
            password: Password,
          });
  
          alert('登录成功！');
          this.$router.push('/waterfall-basic-knowledge');
        } catch (err) {
          alert(err.response?.data || '登录失败');
        }
      },
  
      // 注册逻辑
      async register() {
        const { name, Password } = this;
  
        if (!name || !Password) {
          alert('用户名和密码不能为空');
          return;
        }
  
        try {
          const response = await axios.post('http://localhost:3000/register', {
            username: name,
            password: Password,
          });
  
          alert('注册成功');
          this.$router.push('/waterfall-basic-knowledge');
        } catch (err) {
          alert(err.response?.data || '注册失败');
        }
      },
    },
  };
  </script>
  

<style>
/* 简单样式 */
body {
  font-family: Arial, sans-serif;
  margin: 20px;
  text-align: center; /* 使body内的内容居中 */
}

h1 {
  color: #333;
  text-align: center; /* 居中显示标题 */
}

input {
  margin: 5px;
  padding: 10px;
  font-size: 16px; /* 调整输入框的字体大小 */
  width: 200px; /* 给输入框设置固定宽度 */
}

button {
  margin: 5px;
  padding: 10px 20px; /* 调整按钮的内边距 */
  background-color: #4caf50;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 16px; /* 增加按钮的字体大小 */
  border-radius: 5px; /* 增加圆角效果 */
}

button:hover {
  background-color: #45a049;
}
</style>
  