<template>
  <div class="sidebar">
    <ul>
      <!-- 第一章 -->
      <li>
        <span class="chapter">应用过程</span>
        <ul>
          <!-- 第一章的子章节 -->
          <li @click="goToContent('原则')" class="sub-chapter">原则</li>
          <li @click="goToContent('过程')" class="sub-chapter">过程</li>
        </ul>
      </li>

      <!-- 第二章 -->
      <li>
        <span class="chapter">应用实例</span>
        <ul>
          <!-- 第二章的子章节 -->
          <li @click="goToContent('实例1')" class="sub-chapter">实例1</li>
          <li @click="goToContent('实例2')" class="sub-chapter">实例2</li>
          <li @click="goToContent('实例3')" class="sub-chapter">实例3</li>
          <li @click="goToContent('实例4')" class="sub-chapter">实例4</li>
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
  width: 10%;
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
