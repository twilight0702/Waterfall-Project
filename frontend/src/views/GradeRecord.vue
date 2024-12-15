<template>
  <div class="score-statistics">
    <h1>成绩统计</h1>
    <div class="charts">
      <!-- 时间维度的正确率变化图表 -->
      <div class="chart-container">
        <h2>随时间变化的正确率</h2>
        <canvas id="timeAccuracyChart"></canvas>
      </div>

      <!-- 知识点错误占比 -->
      <div class=" chart-container">
        <h2>不同知识点的错误占比</h2>
        <canvas id="topicErrorChart"></canvas>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import Chart from 'chart.js/auto';
import eventBus from '@/eventBus';

export default {
  name: 'ScoreStatistics',
  data() {
    return {
      examRecords: [],
      accuracyData: [],
      timeLabels: [],
      topicErrorData: [],
      topicLabels: [],
      username: '',
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
    // 获取用户的考试记录并处理数据
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
              if (question.isCorrect === "true") ac++;
              return {
                id: question.id,
                correctAnswer: question.answer,
                userAnswer: question.userAnswer,
                kn: question.kn, // 假设题目包含知识点信息
              };
            });
            return {
              date: date,
              time: time,
              score: exam.score,
              ac: ac,
              all: all,
              accuracy: ac / all, // 计算正确率
              examId: exam.testId,
              questions: questions,
            };
          });

          // 处理统计数据
          this.processStatistics();
        })
        .catch(error => {
          console.error('获取考试记录失败：', error);
        });
    },
    // 处理并生成统计图表所需的数据
    processStatistics() {
      // 时间维度的正确率数据
      this.examRecords.sort((a, b) => a.date.localeCompare(b.date));
      this.timeLabels = this.examRecords.map(exam => exam.date);
      this.accuracyData = this.examRecords.map(exam => exam.accuracy);

      // 知识点维度的错误占比数据
      const topicErrors = {};
      this.examRecords.forEach(exam => {
        exam.questions.forEach(question => {
          if (!topicErrors[question.kn]) {
            topicErrors[question.kn] = { total: 0, errors: 0 };
          }
          if (question.correctAnswer != question.userAnswer) {
            topicErrors[question.kn].errors++;
          }
          topicErrors[question.kn].total++;
        });
      });

      console.log("错误", topicErrors);
      this.topicLabels = Object.keys(topicErrors);
      this.topicErrorData = this.topicLabels.map(kn => {
        return (topicErrors[kn].errors / topicErrors[kn].total) * 100;
      });

      // 渲染图表
      this.renderCharts();
    },
    // 渲染所有统计图表
    renderCharts() {
      // 时间维度的正确率变化图表
      new Chart(document.getElementById('timeAccuracyChart'), {
        type: 'line',
        data: {
          labels: this.timeLabels,
          datasets: [{
            label: '正确率',
            data: this.accuracyData,
            borderColor: 'rgba(75, 192, 192, 1)',
            fill: false,
            tension: 0.1,
          }],
        },
        options: {
          responsive: true,
          scales: {
            x: {
              title: { display: true, text: '考试日期' },
            },
            y: {
              title: { display: true, text: '正确率' },
              min: 0,
              max: 1,
              ticks: {
                callback: function (value) {
                  return (value * 100).toFixed(0) + '%';
                },
              },
            },
          },
        },
      });

      // 知识点错误占比图表
      new Chart(document.getElementById('topicErrorChart'), {
        type: 'pie',
        data: {
          labels: this.topicLabels,
          datasets: [{
            label: '知识点错误占比',
            data: this.topicErrorData,
            backgroundColor: ['#FF5733', '#33FF57', '#3357FF', '#FF33A1', '#FF8C33'],
            borderColor: '#ffffff',
            borderWidth: 1,
          }],
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              position: 'top',
            },
            tooltip: {
              callbacks: {
                label: function (tooltipItem) {
                  return tooltipItem.label + ': ' + tooltipItem.raw.toFixed(2) + '%';
                },
              },
            },
          },
        },
      });
    },
  },
};
</script>

<style scoped>
.score-statistics {
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

h1 {
  font-size: 36px;
  margin-bottom: 20px;
}

h2 {
  text-align: center;
}

.charts {
  display: flex;
  flex-direction: row;
  gap: 40px;
  width: 90%;
}

.chart-container {
  width: 100%;
  max-width: 800px;
}

canvas {
  width: 100%;
  height: 500px;
}
</style>
