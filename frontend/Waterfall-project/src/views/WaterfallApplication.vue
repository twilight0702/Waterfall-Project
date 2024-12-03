<template>
  <div class="waterfall-application">
    <!-- 引入侧边栏组件 -->
    <SideBar @chapter-selected="changeChapter" />

    <!-- 主要内容区域 -->
    <div class="content">
      <!-- 显示加载状态 -->
      <div v-if="loading" class="loading">加载中...</div>

    <!-- 显示章节内容 -->
    <div v-if="chapterContent && chapterContent.name" class="chapter-content">
      <h2>{{ chapterContent.name }}</h2>
      <p class="formatted-text">{{ chapterContent.text }}</p>
    </div>

      <!-- 显示错误信息 -->
      <div v-if="errorMessage" class="error-message">
        <p>{{ errorMessage }}</p>
      </div>
    </div>
  </div>
</template>

<style>
.formatted-text {
  white-space: pre-wrap;
}
</style>

<script>
import axios from 'axios';
import SideBar from './another.vue';

export default {
  name: 'WaterfallApplication',
  components: { SideBar },
  data() {
    return {
      selectedChapter: '原则',  // 默认选择
      chapterContent: null,  // 存储章节内容
      loading: false,       // 控制加载状态
      errorMessage: ''      // 存储错误信息
    };
  },
  methods: {
    // 更改章节时更新 selectedChapter 并加载章节内容
    changeChapter({ chapterName }) {
      console.log('选中的章节：', chapterName);  // 打印章节名称，确保正确
      this.selectedChapter = chapterName;  // 更新章节名称
      this.chapterContent = null;  // 清空当前章节内容
      this.errorMessage = '';  // 清空错误信息

      this.fetchChapterContent(chapterName);  // 请求章节内容
    },

    // 请求章节内容
fetchChapterContent(chapterName) {
  this.loading = true;
  this.errorMessage = '';  // 清空错误信息
  console.log('请求章节内容：', chapterName); // 打印请求的章节名称

  // 请求章节内容的API，传递章节名称
  axios.get(`/knowledge/by_name/${chapterName}`)
    .then(response => {
      console.log('API 响应数据：', response.data); // 打印API响应的数据
      console.log('请求URL：', `/knowledge/by_name/${chapterName}`);

      // 判断返回的章节名称是否和请求的章节名称一致
      if (response.data && response.data.name && response.data.name === chapterName) {
        this.chapterContent = response.data;  // 更新章节内容
      } else {
        // 如果返回的数据章节名称不匹配，显示错误信息
        this.errorMessage = '无法获取当前章节内容。';
      }
    })
    .catch(error => {
      // 请求失败时显示错误信息
      this.errorMessage = '请求失败，请稍后再试。';
    })
    .finally(() => {
      this.loading = false;  // 结束加载
    });
  }
  },
  created() {
    this.fetchChapterContent('原则');  // 默认请求“概念”章节内容
  }
};
</script>


<style scoped>
.waterfall-basic-knowledge {
  display: flex;
  height: 100vh;
}

.content {
  width: 80%;
  box-sizing: border-box;
  overflow-y: auto;
  margin-left: 150px;
}

.loading {
  font-size: 20px;
  color: #007bff;
}

.chapter-content h2 {
  color: #333;
  font-size: 24px;
  margin-bottom: 10px;
}

.chapter-content p {
  color: #000000;
  font-size: 16px;
}

.error-message {
  color: red;
  font-size: 18px;
}

.default-content {
  font-size: 18px;
  color: #888;
}
</style>
