<template>
  <div class="login-page">
    <div class="login-container">
      <h1>登录</h1>
      <div class="login-form">
        <input v-model="name" placeholder="用户名" />
        <input v-model="Password" type="password" placeholder="密码" />
        <div class="button-group">
          <button @click="login">登录</button>
          <button class="register-button" @click="register">注册</button>
        </div>
      </div>
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

    // 在登录成功后将用户名传递到目标路由页面
    this.$router.push({ 
      path: '/waterfall-basic-knowledge',
      state: { username: name } // 将用户名存储在路由的 state 中
    });
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

<style scoped>
/* 背景样式 */
.login-page {
  height: 100vh;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #ffffff, #4f86e6); /* 渐变背景 */
  font-family: 'Arial', sans-serif;
}

/* 登录容器 */
.login-container {
  background: white;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  padding: 40px 30px;
  width: 400px;
  text-align: center;
}

/* 标题样式 */
h1 {
  color: #333;
  font-size: 24px;
  margin-bottom: 20px;
}

/* 表单样式 */
.login-form input {
  width: 100%;
  padding: 12px 15px;
  margin: 10px 0;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 5px;
  outline: none;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.login-form input:focus {
  border-color: #6a11cb;
  box-shadow: 0 0 5px rgba(106, 17, 203, 0.5);
}

/* 按钮组 */
.button-group {
  margin-top: 20px;
}

button {
  width: 45%;
  padding: 12px 15px;
  font-size: 16px;
  color: white;
  background-color: #4caf50;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
}

button:hover {
  transform: scale(1.05);
  background-color: #45a049;
}

/* 注册按钮样式 */
.register-button {
  background-color: #007bff;
}

.register-button:hover {
  background-color: #0056b3;
}
</style>
