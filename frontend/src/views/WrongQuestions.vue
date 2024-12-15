<template>
  <div class="completed-exercises">
    <h1>错题合集</h1>
    <div class="main-content">

      <div class="incorrect-questions">
        <h2>选中的错题：</h2>
        <div class="incorrect-list">
          <div class="incorrect-card" v-for="question in selectedMistakes">
            <p>{{ question.index }}</p>
          </div>
        </div>
        <button v-if="!redoMode && selectedMistakes.length > 0" @click="enterRedoMode" class="redo-btn">
          重做
        </button>
        <button v-if="redoMode" @click="submitQuiz" class="redo-btn">
          提交
        </button>
        <h2 v-if="score != null">分数：{{ score }}</h2>
      </div>


      <div class="question-records">
        <div class="question-list">
          <div class="question-card" v-if="selectedExam.questions.length > 0 && !redoMode"
            v-for="(question, index) in selectedExam.questions" :key="index" @click="addToRedo(question, index)">
            <p>题目{{ index + 1 }}：{{ question.text }}</p>
            <p>知识点：{{ question.kn }}</p>
            <p>A：{{ question.A }}</p>
            <p>B：{{ question.B }}</p>
            <p v-if="question.C">C：{{ question.C }}</p>
            <p v-if="question.D">D：{{ question.D }}</p>
            <p>正确答案：{{ question.correctAnswer }}</p>
            <p>用户答案：{{ question.userAnswer }}</p>
          </div>

          <div class="question-card" v-if="selectedMistakes.length > 0 && redoMode"
            v-for="question in selectedMistakes">
            <p>题目{{ question.index }}：{{ question.text }}</p>
            <p>知识点：{{ question.kn }}</p>
            <div class="choseAnser">
              <span v-if="question.A">
                <input type="radio" :name="'question-' + question.index" :value="'A'" v-model="question.userNewAnswer">
                A：{{ question.A }}
              </span>
              <span v-if="question.B">
                <input type="radio" :name="'question-' + question.index" :value="'B'" v-model="question.userNewAnswer">
                B：{{ question.B }}
              </span>
              <span v-if="question.C">
                <input type="radio" :name="'question-' + question.index" :value="'C'" v-model="question.userNewAnswer">
                C：{{ question.C }}
              </span>
              <span v-if="question.D">
                <input type="radio" :name="'question-' + question.index" :value="'D'" v-model="question.userNewAnswer">
                D：{{ question.D }}
              </span>

            </div>
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
      },
      selectedMistakes: [],
      username: '-114514',
      redoMode: false,
      questionsForSubmit: [],
      score: null,
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
                id: id,
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
    addToRedo(item, index) {
      item.index = index + 1;
      item.userNewAnswer = null;
      console.log("item:", item);
      if (this.selectedMistakes.includes(item)) {
        this.selectedMistakes.splice(this.selectedMistakes.indexOf(item), 1);
      } else
        this.selectedMistakes.push(item);
    },
    enterRedoMode() {
      // 进入重做模式，更新界面为做题状态
      this.redoMode = true;
    },
    submitQuiz() {
      let totalScore = 0;
      this.questionsForSubmit = []; // 清空错误题目
      const timestampInSeconds = Math.floor(Date.now() / 1000);
      let testData = {
        results: [],
        score: 0,
        username: this.username.toString(),
        testId: timestampInSeconds.toString(),
      }
      console.log("username:", this.username);
      // 遍历所有题目，检查答案
      this.selectedMistakes.forEach((item) => {
        let singleData = {
          id: item.id,
          userAnswer: item.userNewAnswer || "none",
          answer: item.correctAnswer,
          isCorrect: (item.userNewAnswer === item.correctAnswer).toString(),
        };
        if (singleData.isCorrect == "true") {
          totalScore += 10; // 每道题10分
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

.incorrect-questions {
  width: 100%;
  display: flex;
  flex-direction: row;
  gap: 20px;
  height: 45px;
  margin-bottom: 10px;
}

.incorrect-list {
  background-color: rgb(95, 210, 179);
  border-radius: 20px;
  display: flex;
  flex-direction: row;
  gap: 5px;
  height: 100%;
  width: 70%;
  padding-left: 10px;
  padding-top: 5px;
  padding-bottom: 5px;
}

.incorrect-card {
  background-color: #f8f8f8;
  border-radius: 16px;
  align-self: center;
  text-align: center;
  width: 30px;
  padding: 5px;
  cursor: pointer;
}

.redo-btn {
  font-size: 20px;
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.redo-btn:hover {
  background-color: #45a049;
}

.question-records {
  height: 70vh;
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

.choseAnser {
  display: flex;
  flex-direction: row;
  gap: 30px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 10px;
  background-color: #f8f8f8;
  margin: 5px;
}

input {
  appearance: none;
  border: 1px solid #ccc;
  border-radius: 20%;
  align-self: center;
  width: 20px;
  height: 20px;
  cursor: pointer;
  position: relative;
}

input:checked::before {
  content: '✔';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 14px;
  color: green;
}
</style>
