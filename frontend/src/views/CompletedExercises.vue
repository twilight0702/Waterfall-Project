<template>
  <div class="completed-exercises">
    <h1>已做习题</h1>
    <div class="main-content">

      <div class="exam-records">
        <h2>考试记录</h2>
        <div class="exam-list">
          <div class="exam-card" v-for="(exam, index) in examRecords" :key="index" @click="selectExam(exam.questions)">
            <p>日期：{{ exam.date }}</p>
            <p>时间：{{ exam.time }}</p>
            <p>分数：{{ exam.score }}</p>
            <p>正确率：{{ exam.ac }}/{{ exam.all }}</p>
          </div>
        </div>
      </div>

      <div class="question-records">
        <h2>题目列表</h2>
        <div class="question-list">
          <div class="question-card" v-if="selectedExam.questions.length > 0"
            v-for="(question, index) in selectedExam.questions" :key="index">
            <p>题目{{ index + 1 }}：{{ question.text }}</p>
            <p>知识点：{{ question.kn }}</p>
            <p>A：{{ question.A }}</p>
            <p>B：{{ question.B }}</p>
            <p v-if="question.C">C：{{ question.C }}</p>
            <p v-if="question.D">D：{{ question.D }}</p>
            <p>正确答案：{{ question.correctAnswer }}</p>
            <p>用户答案：{{ question.userAnswer }}</p>
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
  name: 'CompletedExercises',
  data() {
    return {
      // 假设的考试记录数据，实际数据通过接口获取
      examRecords: [],
      selectedExam: {
        questions: []
      }, // 用于存储选中的考试记录
      username: '-114514',
    };
  },
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
          this.getExams(response.data);
        }
      })
      .catch(error => {
        console.error('获取用户信息失败：', error);
      });
  },
  methods: {
    getExams(name) {
      axios.get(`/knowledge/getUserTests?userName=${name}`)
        .then(response => {
          console.log('获取考试记录成功：', response.data);
          let exams = response.data;
          this.examRecords = exams.map(exam => {
            let dateObj = new Date(exam.testID * 1000);
            let date = dateObj.toISOString().split('T')[0];
            let time = dateObj.toTimeString().split(' ')[0];
            let ac = 0, all = exam.results.length;
            let questions = exam.results.map(question => {
              if (question.isCorrect == "true") ac++;
              return {
                id: question.id,
                correctAnswer: question.answer,
                userAnswer: question.userAnswer,
              };
            });
            return {
              date: date,
              time: time,
              score: exam.score,
              ac: ac,
              all: all,
              examId: exam.testId,
              questions: questions,
            };
          });
        })
        .catch(error => {
          console.error('获取考试记录失败：', error);
        });
    },
    selectExam(questions) {
      this.selectedExam.questions = [];
      questions.forEach(question => {
        let id = question.id;
        axios.get(`/knowledge/getTestById?id=${id}`)
          .then(response => {
            let data = response.data;
            let singleQuestion = {
              text: data.text,
              kn: data.kn,
              A: data.A,
              B: data.B,
              C: data.C,
              D: data.D,
              correctAnswer: question.correctAnswer,
              userAnswer: question.userAnswer,
            };
            this.selectedExam.questions.push(singleQuestion);
          })
      });
    },
  },
};
</script>

<style scoped>
.completed-exercises {
  margin-top: 30px;
  padding: 20px;
}

.completed-exercises h1 {
  text-align: center;
  font-weight: bold;
  font-size: 36px;
  margin-bottom: 20px;
}

.main-content {
  display: flex;
  justify-content: space-between;
  width: 90vw;
}

.exam-records {
  flex: 0 0 18%;
}

.question-records {
  flex: 0 0 78%;
}

.exam-records,
.question-records {
  height: calc(85vh - 50px);
}

.exam-records h2,
.question-records h2 {
  text-align: center;
  font-weight: bold;
  font-size: 30px;
  margin-bottom: 15px;
}

.exam-list,
.question-records .question-list {
  background-color: #9cc2bc;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  height: 90%;
  overflow-y: auto;
  scrollbar-width: none;
  padding-top: 20px;
  border: 10px solid #9cc2bc; 

}

.exam-list {
  padding-right: 10px;
  padding-left: 10px;
}

.exam-card,
.question-records .question-list .question-card {
  background-color: #f8f8f8;
  border: 1px solid #ddd;
  border-radius: 16px;
  width: 90%;
  align-self: center;
  padding: 15px;
  cursor: pointer;
  width: 95%;
}

.exam-card:hover {
  transition: transform 0.2s ease-in-out;
  transform: scale(1.05);
}

h1 {
  background-color:#86b1ab ;
  max-width: 300px;
  border-radius: 15px; /* 添加圆角 */
  text-align: center; /* 文本内容居中 */
  margin: 0 auto; /* 块级元素水平居中 */
  color:#ffffff;
}

h2 {
  color:#495b5b;
}

</style>
