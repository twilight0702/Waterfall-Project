<template>
  <div class="sidebar">
    <ul>
      <!-- 第一章 -->
      <li>
        <span class="chapter">瀑布模型的概念</span>
        <ul>
          <li @click="goToContent('概念')" class="sub-chapter">概念</li>
          <li @click="goToContent('概念习题')" class="sub-chapter">概念习题</li>
        </ul>
      </li>

      <!-- 第二章 -->
      <li>
        <span class="chapter">瀑布模型的背景与发展历史</span>
        <ul>
          <li @click="goToContent('起源')" class="sub-chapter">起源</li>
          <li @click="goToContent('演变')" class="sub-chapter">演变</li>
          <li @click="goToContent('相关习题')" class="sub-chapter">相关习题</li>
        </ul>
      </li>

      <!-- 第三章 -->
      <li>
        <span class="chapter">瀑布模型的优缺点</span>
        <ul>
          <li @click="goToContent('优点')" class="sub-chapter">优点</li>
          <li @click="goToContent('缺点')" class="sub-chapter">缺点</li>
          <li @click="goToContent('优缺点习题')" class="sub-chapter">优缺点习题</li>
        </ul>
      </li>

      <!-- 第四章 -->
      <li>
        <span class="chapter">瀑布模型的相关理论</span>
        <ul>
          <li @click="goToContent('变体-V模型')" class="sub-chapter">变体-V模型</li>
          <li @click="goToContent('螺旋模型')" class="sub-chapter">螺旋模型</li>
          <li @click="goToContent('敏捷开发')" class="sub-chapter">敏捷开发</li>
        </ul>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'SideBar',
  methods: {
    // 修改 goToContent 方法以发送章节名称并输出章节名称
    goToContent(chapterName) {
      this.$emit('chapter-selected', { chapterName });  // 发送章节名称
    }
  }
};
</script>

<style scoped>
.sidebar {
  position: fixed;
  left: 0;
  width: 15%;
  height: 100vh;
  background-color: #f0fff2;
  padding: 10px;
  border-right: 2px solid #ddd;
  top: 44px;
}

.sidebar ul {
  list-style-type: none;
  padding: 0;
}

.sidebar li {
  padding: 8px;
}

.chapter {
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
}

.sub-chapter {
  font-size: 14px;
  margin-left: 20px;
  cursor: pointer;
}

.sub-chapter:hover {
  text-decoration: underline;
}
</style>
