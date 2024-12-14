<template>
  <div class="waterfall-model-framework">
    <!-- 嵌入 HTML 文件 -->
    <iframe src="./framework.html" class="framework-iframe" title="知识框架图谱">
    </iframe>
  </div>
</template>

<script>
export default {
  name: 'WaterfallModelFramework',
  created() {
    this.$getUser();
  },
};
</script>

<style scoped>
.waterfall-model-framework {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  /*padding: 20px;*/
}

.framework-iframe {
  width: 1750px;
  height: 90vh;
  /* 根据页面需求调整高度 */
  border: none;
  /* 去除 iframe 边框 */
}
</style>
