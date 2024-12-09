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
      <!-- 显示章节内容 -->
      <div v-if="chapterContent && chapterContent.name" class="chapter-content">
        <h2>{{ chapterContent.name }}</h2>
        <p class="formatted-text" id="my-text">{{ chapterContent.text }}</p>
        <!-- 显示对应图片 -->
        <div v-if="chapterImage" class="chapter-image">
        <img :src="chapterImage" alt="章节图片" @error="onImageError" />
        </div>
      </div>

      <!-- 显示错误信息 -->
      <div v-if="errorMessage" class="error-message">
        <p>{{ errorMessage }}</p>
      </div>
    </div>
  </div>
</template>

<style scoped>


.content {
  position: fixed;
  top: 100px;
  left: max(20%, 270px);
  right: 2%;
  display: flex;
}

.chapter-content {
  width: 100%; /* 根据容器自动适配宽度 */
  max-height: 80vh; /* 限制最大高度，防止溢出 */
  margin: 0 auto; /* 居中容器 */
  padding: 20px;
  overflow-y: auto; /* 启用垂直滚动 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 添加阴影以区分内容块 */
  background-color: #fff; /* 白色背景 */
  border-radius: 8px; /* 添加圆角 */
  border: 1px solid #ddd; /* 边框 */
}

.chapter-content h2 {
  font-size: 32px; /* 设置标题字号 */
  text-align: center; /* 标题居中 */
  margin-bottom: 20px; /* 增加下方间距 */
  color: #333; /* 标题文字颜色 */
}

.chapter-content .formatted-text {
  font-size: 20px; /* 设置文本字号 */
  line-height: 1.6; /* 行高增强可读性 */
  color: #555; /* 文本颜色 */
  white-space: pre-wrap; /* 保留换行和空格格式 */
}

/* 搜索框样式 */
.search-box {
  position: fixed;
  top: 50px;
  right: 2%;
  z-index: 100;
  width: 100%;
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

/* 章节图片样式 */

.chapter-image {
  display: flex;
  justify-content: flex-start; 
  align-items: center; /* 垂直居中对齐 */
  height: 100%; /* 确保容器有高度 */
}

.chapter-image img {
  max-width: 100%; /* 让图片宽度适应屏幕 */
  max-height: 70vh; /* 限制图片的最大高度 */
  object-fit: contain; /* 保持图片比例 */
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
      selectedChapter: '瀑布模型的起源',  // 默认选择“瀑布模型的概念”
      chapterContent: null,  // 存储章节内容
      chapterImage: '',      // 存储章节图片路径
      errorMessage: '',      // 存储错误信息
      searchQuery: '',       // 搜索框内容绑定
    };
  },
  methods: {
    // 更改章节时更新 selectedChapter 并加载章节内容
    changeChapter({ chapterName }) {
      this.selectedChapter = chapterName;  // 更新章节名称
      this.chapterContent = null;  // 清空当前章节内容
      this.chapterImage = '';     // 清空章节图片
      this.errorMessage = '';     // 清空错误信息

      // 同时获取章节内容和图片
      this.fetchChapterContent(chapterName);
      this.setChapterImage(chapterName);
    },

    // 请求章节内容
    fetchChapterContent(chapterName) {
      this.errorMessage = '';  // 清空错误信息
      console.log('请求章节内容：', chapterName); // 打印请求的章节名称

      // 模拟请求章节内容，替换为实际接口请求
      axios.get(`/knowledge/by_name/${chapterName}`)
        .then(response => {
          if (response.data && response.data.name && response.data.name === chapterName) {
            this.chapterContent = response.data;  // 更新章节内容
            const formattedText = this.chapterContent.text
              .split('\n')
              .map(line => {
                if (line.length < 15) return `${line}`;
                else return `\t${line}`;
              })
              .join('\n\n');
            this.chapterContent.text = formattedText;
          } else {
            this.errorMessage = '无法获取当前章节内容。';
          }
        })
        .catch(error => {
          this.errorMessage = '请求失败，请稍后再试。';
        });
    },

    // 设置章节图片
    setChapterImage(chapterName) {
      // 章节名称与图片路径的映射
      const imageMap = {
        '瀑布模型的起源': '/images/waterfall-origin.jpg', // 示例：瀑布模型的起源章节对应的图片
        '优势': '/images/waterfall-advantage.jpg',
        '常见问题及应对策略': '/images/waterfall-boundedness.jpg',
        '与V模型': '/images/V-model.jpg',
        '与螺旋模型': '/images/spin-model.jpg',
        '系统设计': '/images/system-design.jpg',
      };

      // 设置 chapterImage
      this.chapterImage = imageMap[chapterName] || ''; // 如果没有找到对应图片，设置为空字符串
      console.log("当前章节图片路径：", this.chapterImage); // 打印图片路径用于调试
    },

    // 搜索章节
    searchChapter() {
      // 当点击搜索按钮时，根据查询进行章节跳转
      if (this.searchQuery) {
        this.fetchChapterContent(this.searchQuery);
        this.setChapterImage(this.searchQuery);  // 根据搜索结果设置图片
      } else {
        this.chapterContent = null;  // 如果没有输入内容，则清空章节内容
        this.chapterImage = '';      // 清空图片
      }
    },

    // 图片加载错误处理
    onImageError() {
      console.error('图片加载失败:', this.chapterImage);
      this.errorMessage = '图片加载失败，请检查图片路径。';
    }
  },
  created() {
    // 初始加载时获取默认章节的内容和图片
    this.fetchChapterContent(this.selectedChapter);
    this.setChapterImage(this.selectedChapter);
  },

  watch: {
    // 监听selectedChapter的变化，确保章节变化时能够更新内容和图片
    selectedChapter(newChapter) {
      this.fetchChapterContent(newChapter);
      this.setChapterImage(newChapter);
    }
  }
};
</script>