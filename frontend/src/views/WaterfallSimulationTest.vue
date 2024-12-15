<template>
  <div>
    <!-- 选择题目数量和知识点范围 -->
    <div v-if="!isQuizStarted">
      <div class="form-group">
        <div>
          <label for="questionCount">题目数量:</label>
          <input type="number" id="questionCount" v-model="questionCount" min="1" />
        </div>
      </div>

      <div class="form-group">
        <label for="knowledgeRange">知识点范围:</label>
        <div class="button-grid">
          <!-- 使用 v-for 渲染知识点范围的按钮 -->
          <button v-for="option in knowledgeOptions" :key="option.id"
            :class="['option-button', { selected: selectedKnowledgePoints.includes(option) }]"
            @click="toggleKnowledgePoint(option)">
            {{ option.name }}
          </button>
        </div>

      </div>

      <button @click="startQuiz">确 认</button>
    </div>

    <div class="tests-container">
      <!-- 显示测验内容 -->
      <div v-if="isQuizStarted">
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
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import eventBus from '@/eventBus';

export default {
  created() {
    const hash = this.$getHash();
    axios.get(`/knowledge/findUser?key=${hash}`)
      .then(response => {
        console.log('获取用户信息成功：', response.data);
        if (response.data == "-114514")
          window.location.href = window.location.origin + '/login';
        else {
          eventBus.emit('usernameUpdated', response.data);
          this.username = response.data;
        }
      })
      .catch(error => {
        console.error('获取用户信息失败：', error);
      });
  },
  data() {
    return {
      questionCount: 1, // 默认题目数量为1
      selectedKnowledgePoints: [], // 存储选中的知识点
      isQuizStarted: false, // 控制是否显示测验内容
      data: [], // 用于存储从接口获取的数据
      score: null, // 存储成绩
      incorrectAnswers: [], // 存储错误题目的信息
      knowledgeOptions: [
        { id: 1, name: '定义与概述' },
        { id: 2, name: '阶段划分' },
        { id: 3, name: '特点与优势' },
        { id: 4, name: '常见问题及应对策略' },
        { id: 5, name: '与其他模型的比较' },
        { id: 6, name: '应用案例' },
        { id: 7, name: '实施最佳实践' },
        { id: 8, name: '改进方向' },
        { id: 9, name: '总结与反思' },
      ], // 初始知识点选项
      username: "-114514",
    };
  },
  methods: {

    toggleKnowledgePoint(option) {
      // 切换知识点选中状态
      const index = this.selectedKnowledgePoints.findIndex((item) => item.id === option.id);
      if (index === -1) {
        this.selectedKnowledgePoints.push(option);
      } else {
        this.selectedKnowledgePoints.splice(index, 1);
      }
      console.log("当前选中的知识点：", this.selectedKnowledgePoints.map(item => item.name));
    },


    // 获取数据，根据题目数量和知识点范围
    fetchData() {
      console.log("获取题目数量：" + this.questionCount + " 知识点范围：" + this.selectedKnowledgePoints.map(item => item.name));

      axios
        .get('/knowledge/random-tests-has-limits', {
          params: {
            limit: this.questionCount,
            labels: this.selectedKnowledgePoints.map(item => item.name),
          },
          paramsSerializer: (params) => {
            const query = Object.keys(params)
              .map((key) => {
                if (Array.isArray(params[key])) {
                  return params[key].map((val) => `${key}=${encodeURIComponent(val)}`).join('&');
                }
                return `${key}=${encodeURIComponent(params[key])}`;
              })
              .join('&');
            console.log("题目访问请求:", query);
            return query;
          },
        })
        .then((response) => {
          this.data = response.data; // 假设接口返回 JSON 数据
        })
        .catch((error) => {
          console.error('获取数据失败:', error);
        });
    },

    // 用户点击确认后开始测验
    startQuiz() {
      if (this.questionCount <= 0 || this.selectedKnowledgePoints.length === 0) {
        alert('请输入有效的题目数量和选择至少一个知识点');
        return;
      }

      // 根据选择的数据获取题目
      this.fetchData();

      // 开始测验
      this.isQuizStarted = true;
    },

    // 提交测验并计算成绩
    submitQuiz() {
      let totalScore = 0;
      this.incorrectAnswers = []; // 清空错误题目
      const timestampInSeconds = Math.floor(Date.now() / 1000);
      let testData = {
        results: [],
        score: 0,
        username: this.username.toString(),
        testId: timestampInSeconds.toString(),
      }
      console.log("username:", this.username);
      // 遍历所有题目，检查答案
      this.data.forEach((item) => {
        let singleData = {
          id: item.properties.id,
          userAnswer: item.userAnswer || "none",
          answer: item.properties.answer,
          isCorrect: (item.userAnswer === item.properties.answer).toString(),
        };
        if (item.userAnswer === item.properties.answer) {
          totalScore += 10; // 每道题10分
        } else {
          // 记录错误题目和正确答案
          this.incorrectAnswers.push({
            text: item.properties.text,
            userAnswer: item.userAnswer,
            answer: item.properties.answer,
          });
        }
        testData.results.push(singleData);
      });
      testData.score = totalScore.toString();
      let testData_json = JSON.stringify(testData);
      console.log("提交的测试数据：", testData_json);

      axios.post("/knowledge/submit-test", testData_json, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
        .then((response) => {
          if (response.data === "success") {
            console.log("提交成功");
          }
          else throw new Error("提交失败");
        })
        .catch((error) => {
          console.error("提交失败", error);
        });

      this.score = totalScore; // 更新成绩
    },
  },
};
</script>

<style scoped>
/* 样式部分 */
button {
  margin-top: 20px;
  font-size: large;
  height: 50px;
  width: 350px;
  background-color: rgb(33, 90, 136);
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 10px;

  display: block;
  /* 转换为块级元素 */
  margin: 0 auto;
  /* 设置左右外边距为 auto 实现水平居中 */
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
  margin-bottom: 20px;
}

.question-item p {
  margin-bottom: 10px;
}

li {
  margin-bottom: 8px;
}

input[type="radio"] {
  margin-right: 8px;
}

.form-group {
  margin-bottom: 20px;
  width: 500px;
}

.form-group label,
.form-group input {
  font-size: large;
  display: inline-flex;
  margin-bottom: 10px;
  align-items: center;
  margin-left: 5%;
}

.form-group input {
  width: auto;
  margin-left: 20px;
  height: 40px;
  padding: 8px;
}

.button-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 10px;
}

.option-button {
  font-size: large;
  width: 140px;
  height: 70px;
  background-color: #38c4b3;
  box-shadow: 4px 4px 10px rgba(54, 124, 118, 0.2);
  border-radius: 10px;
  cursor: pointer;
  text-align: center;
  transition: background-color 0.3s;
}

.option-button.selected {
  background-color: #1f9dce;
  /* 选中时的背景色 */
  color: white;
}

.option-button:hover {
  background-color: #1f9dce;
}

.tests-container {
  margin-top: 60px;
  /* 设置组件离页面顶部的距离 */
  max-width: 800px
}
</style>
