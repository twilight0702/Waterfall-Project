<template>
  <div>
    <ul>
      <!-- 遍历每个题目 -->
      <li v-for="(item, index) in data" :key="index" class="question-item">
        <p><strong>题目 {{ index + 1 }}：</strong>{{ item.properties.text }}</p>
        <ul>
          <!-- 动态渲染选项，使用 radio 按钮 -->
          <li v-if="item.properties.A">
            <input type="radio" :name="'question-' + index" :value="'A'" v-model="item.userAnswer">
            <strong>A:</strong> {{ item.properties.A }}
          </li>
          <li v-if="item.properties.B">
            <input type="radio" :name="'question-' + index" :value="'B'" v-model="item.userAnswer">
            <strong>B:</strong> {{ item.properties.B }}
          </li>
          <li v-if="item.properties.C">
            <input type="radio" :name="'question-' + index" :value="'C'" v-model="item.userAnswer">
            <strong>C:</strong> {{ item.properties.C }}
          </li>
          <li v-if="item.properties.D">
            <input type="radio" :name="'question-' + index" :value="'D'" v-model="item.userAnswer">
            <strong>D:</strong> {{ item.properties.D }}
          </li>
        </ul>
      </li>
    </ul>

    <!-- 提交按钮，点击后计算成绩 -->
    <button @click="submitQuiz">提交</button>

    <!-- 显示成绩 -->
    <div v-if="score !== null">
      <p>您的成绩是：{{ score }} 分</p>
      <!-- 显示错误题目和正确答案 -->
      <div v-if="incorrectAnswers.length > 0">
        <h3>错误题目：</h3>
        <ul>
          <li v-for="(item, index) in incorrectAnswers" :key="index">
            <p><strong>题目：</strong>{{ item.text }}</p>
            <p><strong>您的答案：</strong>{{ item.userAnswer }} <span style="color:red;">(错误)</span></p>
            <p><strong>正确答案：</strong>{{ item.answer }}</p>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      data: [], // 用于存储从接口获取的数据
      score: null, // 存储成绩
      incorrectAnswers: [], // 存储错误题目的信息
    };
  },
  methods: {
    // 请求数据
    fetchData() {
      axios
        .get('/knowledge/random-tests?limit=10') // 使用代理路径
        .then((response) => {
          this.data = response.data; // 假设接口返回 JSON 数据
        })
        .catch((error) => {
          console.error('获取数据失败:', error);
        });
    },

    // 提交时核对答案并计算成绩
    submitQuiz() {
      let totalScore = 0;
      this.incorrectAnswers = []; // 清空之前记录的错误题目

      // 遍历所有题目，检查用户选择的答案与正确答案是否匹配
      this.data.forEach((item) => {
        if (item.userAnswer === item.properties.answer) {
          totalScore += 10; // 每道题对加 10 分
        } else {
          // 如果答案不正确，将错误题目和正确答案记录下来
          this.incorrectAnswers.push({
            text: item.properties.text,
            userAnswer: item.userAnswer,
            answer: item.properties.answer,
          });
        }
      });

      this.score = totalScore; // 更新成绩
    },
  },
  mounted() {
    this.fetchData(); // 组件加载时调用
  },
};
</script>

<style scoped>
/* 样式可以根据需要调整 */
button {
  margin-top: 20px;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

ul {
  list-style-type: none;
  padding: 0;
}

h3 {
  color: red;
}

.question-item {
  margin-bottom: 20px; /* 每个题目之间的间隔 */
}

.question-item p {
  margin-bottom: 10px; /* 题目和选项之间的间隔 */
}

li {
  margin-bottom: 8px; /* 每个选项之间的间隔 */
}

/* 选项样式 */
input[type="radio"] {
  margin-right: 8px;
}
</style>
