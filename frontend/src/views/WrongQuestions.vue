<template>
  <div class="completed-exercises">
    <h1>错题合集</h1>
    <div class="main-content">
      <div class="question-records">
        <div class="question-list">
          <div class="question-card" v-if="selectedExam.questions.length > 0"
            v-for="(question, index) in selectedExam.questions" :key="index">
            <p>题目{{ index + 1 }}：{{ question.text }}</p>
            <p>知识点：{{ question.kn }}</p>
            <p>A：{{ question.A }}</p>
            <p>B：{{ question.B }}</p>
            <p>C：{{ question.C }}</p>
            <p>D：{{ question.D }}</p>
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
          this.showMistakes();
        })
        .catch(error => {
          console.error('获取考试记录失败：', error);
        });
    },
    showMistakes() {
      this.selectedExam.questions = [];
      this.examRecords.forEach(exam => {
        exam.questions.forEach(question => {
          let id = question.id;
          if (question.correctAnswer == question.userAnswer) return;
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
      })
    },
  },
};
</script>

<style scoped>
.completed-exercises {
  padding: 20px;
}

.completed-exercises h1 {
  text-align: center;
  font-weight: bold;
  font-size: 36px;
  margin-bottom: 20px;
}

.main-content {
  width: 90vw;
}

.question-records {
  height: calc(85vh - 50px);
}

.question-list {
  background-color: rgb(143, 198, 143);
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  height: 100%;
  overflow-y: auto;
  scrollbar-width: none;
  padding-top: 20px;

}

.question-card {
  background-color: #f8f8f8;
  border: 1px solid #ddd;
  border-radius: 16px;
  width: 90%;
  align-self: center;
  padding: 15px;
  cursor: pointer;
  width: 95%;
}
</style>
