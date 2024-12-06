<template>
  <div>
    <!-- 选择题目数量和知识点范围 -->
    <div v-if="!isQuizStarted">
      <div class="form-group">
        <label for="questionCount">题目数量:</label>
        <input type="number" id="questionCount" v-model="questionCount" min="1" />
      </div>

      <div class="form-group">
        <label for="knowledgeRange">知识点范围:</label>
        <!-- 使用 vue-multiselect 实现多选下拉框 -->
        <multiselect 
          v-model="selectedKnowledgePoints"
          :options="knowledgeOptions"
          :multiple="true"
          :closeOnSelect="false"
          placeholder="请选择知识点范围"
          track-by="name"
          label="name"
          :allow-empty="false"
        >
          <!-- 自定义选中项的显示 -->
          <template #tag="{ option, remove }">
            <span class="custom-tag">
              {{ option.name }}
              <span class="remove-tag" @click="remove(option)">×</span>
            </span>
          </template>
          
        </multiselect>
      </div>

      <button @click="startQuiz">确认</button>
    </div>

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
</template>

<script>
import axios from 'axios';
import Multiselect from 'vue-multiselect';  // 引入 vue-multiselect 组件

export default {
  components: {
    Multiselect  // 注册组件
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
        { name: '定义与概述' },
        { name: '阶段划分' },
        { name: '特点与优势' },
        { name: '局限性' },
        { name: '常见问题及应对策略' },
        { name: '与其他模型的比较' },
        { name: '应用案例' },
        { name: '实施最佳实践' },
        { name: '改进方向' },
        { name: '总结与反思' },
      ], // 初始知识点选项
    };
  },
  methods: {
    // 获取数据，根据题目数量和知识点范围
    fetchData() {
      axios
        .get('/knowledge/random-tests', {
          params: {
            limit: this.questionCount, // 根据题目数量
            range: this.selectedKnowledgePoints.join(','), // 根据多个选择的知识点
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

      // 遍历所有题目，检查答案
      this.data.forEach((item) => {
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
  height: 50px;
  width: 300px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 10px;
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
  width: 300px;
}

.form-group label {
  display: block;
  margin-bottom: 10px;
}

.form-group input {
  width: 100%;
  height: 40px;
  padding: 8px;
}

.value-input {
  width: 100%;
  padding: 8px;
}

.custom-tag {
  background-color: #007bff;
  color: white;
  border-radius: 4px;
  padding: 3px 8px;
  margin: 1px 5px 5px 0;
  display: inline-block;
}

.remove-tag {
  margin-left: 5px;
  cursor: pointer;
  font-weight: bold;
}
</style>
