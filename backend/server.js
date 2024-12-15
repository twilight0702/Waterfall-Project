const express = require('express');
const bcrypt = require('bcryptjs');
const csvParser = require('csv-parser');
const fs = require('fs');
const jsonfile = require('jsonfile');
const path = require('path');
const cors = require('cors');  // 引入 cors 模块
const { constrainedMemory } = require('process');

const app = express();
app.use(express.json());
// 启用 CORS 中间件，允许所有域访问
app.use(cors());

const usersCsvPath = path.join(__dirname, 'assets', 'users.csv');
const usersDataDir = path.join(__dirname, 'assets', 'users_data');

// 登录接口
app.post('/login', async (req, res) => {
  const { username, password } = req.body;

  try {
    // 读取 users.csv 文件
    const users = await readUsersFromCsv();

    const user = users.find(u => u.username === username);

    if (!user) {
      return res.status(401).send('用户名或密码错误');
    }

    // 验证密码
    const passwordMatch = await bcrypt.compare(password, user.password);
    if (passwordMatch) {
      // 如果密码正确，返回成功响应
      //const userDataPath = path.join(usersDataDir, `${username}.json`);
      //const userData = jsonfile.readFileSync(userDataPath);
      return res.status(200).json({ message: '登录成功' });
    } else {
      return res.status(401).send('用户名或密码错误');
    }
  } catch (err) {
    console.error('登录失败:', err);
    res.status(500).send('登录失败，请稍后再试');
  }
});

// 注册接口
app.post('/register', async (req, res) => {
  const { username, password } = req.body;

  try {
    // 读取 users.csv 文件
    const users = await readUsersFromCsv();

    // 检查用户是否已经存在
    if (users.find(u => u.username === username)) {
      return res.status(400).send('用户名已存在');
    }

    // 加密密码
    const hashedPassword = await bcrypt.hash(password, 10);

    // 添加新用户到 CSV 文件
    const newUser = { username, password: hashedPassword };
    await appendUserToCsv(newUser);

    res.status(200).send('注册成功');
  } catch (err) {
    console.error('注册失败:', err);
    res.status(500).send('注册失败，请稍后再试');
  }
});

// 从 CSV 文件读取用户数据
function readUsersFromCsv() {
  return new Promise((resolve, reject) => {
    const users = [];
    fs.createReadStream(usersCsvPath)
      .pipe(csvParser())
      .on('data', (row) => {
        users.push(row);
      })
      .on('end', () => {
        console.log('所有用户数据:', users); // 调试输出
        resolve(users);
        console.log('所有用户数据:', users); // 调试输出
      })
      .on('error', (err) => {
        reject(err);
      });
  });
}

// 将用户数据追加到 CSV 文件
function appendUserToCsv(user) {
  return new Promise((resolve, reject) => {
    const userData = `${user.username},${user.password}\n`;
    fs.appendFile(usersCsvPath, userData, (err) => {
      if (err) reject(err);
      resolve();
    });
  });
}

app.listen(3000, () => {
  console.log('Server running on port 3000');
});
