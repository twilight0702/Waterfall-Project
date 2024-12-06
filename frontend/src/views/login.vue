<template>
    <div>
      <h1>
        登录
      </h1>
  
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
  import bcrypt from 'bcryptjs'; // 加密库

  export default {
    data() {
      return {
        name: '',
        Password: '',
        users: this.getUsersFromStorage(), // 从 LocalStorage 加载用户数据
      };
    },
    methods: {
      // 注册逻辑
      async register() {
        const { name, Password, users } = this;

        if (!name || !Password) {
          alert('用户名和密码不能为空');
          return;
        }

        if (users.find(user => user.username === name)) {
          alert('用户名已存在');
          return;
        }

        try {
          // 加密密码
          const hashedPassword = await bcrypt.hash(Password, 10);

          // 添加新用户
          const newUser = { username: name, password: hashedPassword, data: {} };
          users.push(newUser);
          this.saveUsersToStorage(users);

          alert('注册成功');
          this.$router.push('/waterfall-basic-knowledge');
        } catch (err) {
          console.error('注册失败:', err);
          alert('注册失败，请稍后再试');
        }
      },

      // 登录逻辑
      async login() {
        const { name, Password, users } = this;

        if (!name || !Password) {
          alert('用户名和密码不能为空');
          return;
        }

        const user = users.find(user => user.username === name);

        if (!user) {
          alert('用户名或密码错误');
          return;
        }

        try {
          // 验证密码
          const passwordMatch = await bcrypt.compare(Password, user.password);
          if (passwordMatch) {
            //alert(`登录成功！欢迎 ${loginUsername}`);
            this.$router.push('/waterfall-basic-knowledge');
          } else {
            alert('用户名或密码错误');
          }
        } catch (err) {
          console.error('登录失败:', err);
          alert('登录失败，请稍后再试');
        }
      },

      // 从 LocalStorage 读取用户数据
      getUsersFromStorage() {
        const usersJson = localStorage.getItem('users');
        return usersJson ? JSON.parse(usersJson) : [];
      },

      // 将用户数据保存到 LocalStorage
      saveUsersToStorage(users) {
        localStorage.setItem('users', JSON.stringify(users));
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
  