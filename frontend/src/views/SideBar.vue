<template>
  <div class="sidebar">
    <ul>
      <!-- 第一章 -->
      <li>
        <span class="chapter">1.定义与概述</span>
        <ul>
          <li @click="goToContent('瀑布模型的起源')" class="sub-chapter">瀑布模型的起源</li>
          <li @click="goToContent('什么是瀑布模型')" class="sub-chapter">什么是瀑布模型</li>
          <li @click="goToContent('核心特点')" class="sub-chapter">核心特点</li>
          <li @click="goToContent('适用场景')" class="sub-chapter">适用场景</li>
        </ul>
      </li>

      <!-- 第二章 -->
      <li>
        <span class="chapter">2.阶段划分</span>
        <ul>
          <li @click="goToContent('需求分析')" class="sub-chapter">需求分析</li>
          <li @click="goToContent('系统设计')" class="sub-chapter">系统设计</li>
          <li @click="goToContent('实现与编码')" class="sub-chapter">实现与编码</li>
          <li @click="goToContent('测试')" class="sub-chapter">测试</li>
          <li @click="goToContent('部署与发布')" class="sub-chapter">部署与发布</li>
          <li @click="goToContent('维护')" class="sub-chapter">维护</li>
        </ul>
      </li>

      <!-- 第三章 -->
      <li>
        <span class="chapter">3.特点与优势</span>
        <ul>
          <li @click="goToContent('特点')" class="sub-chapter">特点</li>
          <li @click="goToContent('优势')" class="sub-chapter">优势</li>
          <li @click="goToContent('局限性')" class="sub-chapter">局限性</li>
          <li @click="goToContent('常见问题及应对策略')" class="sub-chapter">常见问题及应对策略</li>
        </ul>
      </li>


      <!-- 第四章 -->
      <li>
        <span class="chapter">4.与其他模型的比较</span>
        <ul>
          <li @click="goToContent('与敏捷模型')" class="sub-chapter">与敏捷模型</li>
          <li @click="goToContent('与螺旋模型')" class="sub-chapter">与螺旋模型</li>
          <li @click="goToContent('与V模型')" class="sub-chapter">与V模型</li>
        </ul>
      </li>

      <!-- 第七章 -->
      <li>
        <span class="chapter">5.应用案例</span>
        <ul>
          <li @click="goToContent('成功案例')" class="sub-chapter">成功案例</li>
          <li @click="goToContent('失败案例')" class="sub-chapter">失败案例</li>
        </ul>
      </li>

      <!-- 第八章 -->
      <li>
        <span class="chapter">6.实施最佳实践</span>
        <ul>
          <li @click="goToContent('确保需求文档准确完整')" class="sub-chapter">确保需求文档准确完整</li>
          <li @click="goToContent('增加阶段性交付评审')" class="sub-chapter">增加阶段性交付评审</li>
          <li @click="goToContent('使用原型减少误解')" class="sub-chapter">使用原型减少误解</li>
          <li @click="goToContent('加强文档管理工具')" class="sub-chapter">加强文档管理工具</li>
        </ul>
      </li>

      <!-- 第九章 -->
      <li>
        <span class="chapter">7. 改进方向</span>
        <ul>
          <li @click="goToContent('引入部分敏捷元素')" class="sub-chapter">引入部分敏捷元素</li>
          <li @click="goToContent('增强测试和验证阶段的重视')" class="sub-chapter">增强测试和验证阶段的重视</li>
          <li @click="goToContent('增加跨阶段的回顾与优化')" class="sub-chapter">增加跨阶段的回顾与优化</li>
          
        </ul>
      </li>

      <!-- 第十章 -->
       <li>
        <span class="chapter">8. 总结与反思</span>
        <ul>
          <li @click="goToContent('适用性分析')" class="sub-chapter">适用性分析</li>
          <li @click="goToContent('对传统软件开发的影响')" class="sub-chapter">对传统软件开发的影响</li>
          <li @click="goToContent('面临现代项目的挑战')" class="sub-chapter">面临现代项目的挑战</li>
          
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
  min-width: 250px;
  bottom: 0;
  background-color: #f5f3f3;
  padding: 10px;
  border-right: 2px solid #ddd;
  top: 45px;
  overflow-y: auto; /* 启用垂直滚动 */
  box-sizing: border-box; /* 确保 padding 不影响 height */
}


.sidebar ul {
  list-style-type: none;
  padding: 0;
  margin-left: 10px;
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
