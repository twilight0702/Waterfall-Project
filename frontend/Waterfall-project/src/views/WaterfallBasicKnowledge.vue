<template>
  <div class="waterfall-basic-knowledge">
    <!-- 引入侧边栏组件 -->
    <SideBar @chapter-selected="changeChapter" />

    <!-- 搜索框 -->
    <div class="search-box">
      <input 
        v-model="searchQuery" 
        type="text" 
        placeholder="搜索章节..."
        class="search-input"
      />
      <!-- 搜索按钮 -->
      <button @click="searchChapter" class="search-button">搜索</button>
    </div>

    <!-- 主要内容区域 -->
    <div class="content">
      <!-- 显示加载状态 -->
      <div v-if="loading" class="loading">加载中...</div>

      <!-- 显示章节内容 -->
      <div v-if="chapterContent && chapterContent.name" class="chapter-content">
        <h2>{{ chapterContent.name }}</h2>
        <p class="formatted-text">{{ chapterContent.text }}</p>
      </div>

      <!-- 显示对应图片 -->
      <div v-if="chapterContent && chapterContent.image_url" class="chapter-image">
        <img :src="chapterContent.image_url" alt="Chapter Image">
      </div>

      <!-- 显示错误信息 -->
      <div v-if="errorMessage" class="error-message">
        <p>{{ errorMessage }}</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 搜索框样式 */
.search-box {
  position: fixed;
  top: 50px;
  left: 81%;
  z-index: 100;
  width: 80%;
  max-width: 300px;
  display: flex;
}

.search-input {
  width: 80%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
  outline: none;
}

.search-input:focus {
  border-color: #007bff;
}

.search-button {
  width: 20%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
  cursor: pointer;
}

.search-button:hover {
  background-color: #007bff;
  color: white;
}

/* 其他内容样式保持不变 */
.formatted-text {
  white-space: pre-wrap;
}
</style>

<script>
import axios from 'axios';
import SideBar from './SideBar.vue';

export default {
  name: 'WaterfallBasicKnowledge',
  components: { SideBar },
  data() {
    return {
      selectedChapter: '概念',  // 默认选择“瀑布模型的概念”
      chapterContent: null,  // 存储章节内容
      loading: false,       // 控制加载状态
      errorMessage: '',     // 存储错误信息
      searchQuery: '',      // 搜索框内容绑定
    };
  },
  methods: {
    // 更改章节时更新 selectedChapter 并加载章节内容
    changeChapter({ chapterName }) {
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

      // 模拟请求章节内容，替换为实际接口请求
      axios.get(`/knowledge/by_name/${chapterName}`)
        .then(response => {
          if (response.data && response.data.name && response.data.name === chapterName) {
            this.chapterContent = response.data;  // 更新章节内容
          } else {
            this.errorMessage = '无法获取当前章节内容。';
          }
        })
        .catch(error => {
          this.errorMessage = '请求失败，请稍后再试。';
        })
        .finally(() => {
          this.loading = false;  // 结束加载
        });
    },

    // 搜索章节
    searchChapter() {
      // 当点击搜索按钮时，根据查询进行章节跳转
      if (this.searchQuery) {
        this.fetchChapterContent(this.searchQuery);
      } else {
        this.chapterContent = null;  // 如果没有输入内容，则清空章节内容
      }
    }
  },
  created() {
    // 不再需要默认章节，直接等待用户点击搜索
  }
};
</script>
