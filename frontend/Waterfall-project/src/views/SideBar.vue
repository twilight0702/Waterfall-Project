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
          <li @click="goToContent('第一章习题')" class="sub-chapter">第一章习题</li>
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
          <li @click="goToContent('第二章习题')" class="sub-chapter">第二章习题</li>
        </ul>
      </li>

      <!-- 第三章 -->
      <li>
        <span class="chapter">3.特点与优势</span>
        <ul>
          <li @click="goToContent('特点')" class="sub-chapter">特点</li>
          <li @click="goToContent('优势')" class="sub-chapter">优势</li>
          <li @click="goToContent('第三章习题')" class="sub-chapter">第三章习题</li>
        </ul>
      </li>

      <!-- 第四章 -->
      <li>
        <span class="chapter">4.局限性</span>
        <ul>
          <li @click="goToContent('缺乏灵活性')" class="sub-chapter">缺乏灵活性</li>
          <li @click="goToContent('需求变更成本更高')" class="sub-chapter">需求变更成本更高</li>
          <li @click="goToContent('阶段依赖性强')" class="sub-chapter">阶段依赖性强</li>
          <li @click="goToContent('客户参与度低')" class="sub-chapter">客户参与度低</li>
          <li @click="goToContent('第四章习题')" class="sub-chapter">第四章习题</li>
        </ul>
      </li>

      <!-- 第五章 -->
      <li>
        <span class="chapter">5.常见问题及应对策略</span>
        <ul>
          <li @click="goToContent('问题与应对1')" class="sub-chapter">问题与应对1</li>
          <li @click="goToContent('问题与应对2')" class="sub-chapter">问题与应对2</li>
          <li @click="goToContent('问题与应对3')" class="sub-chapter">问题与应对3</li>
          <li @click="goToContent('第五章习题')" class="sub-chapter">第五章习题</li>
        </ul>
      </li>

      <!-- 第六章 -->
      <li>
        <span class="chapter">6.与其他模型的比较</span>
        <ul>
          <li @click="goToContent('与敏捷模型')" class="sub-chapter">与敏捷模型</li>
          <li @click="goToContent('与螺旋模型')" class="sub-chapter">与螺旋模型</li>
          <li @click="goToContent('与V模型')" class="sub-chapter">与V模型</li>
          <li @click="goToContent('第六章习题')" class="sub-chapter">第六章习题</li>
        </ul>
      </li>

      <!-- 第七章 -->
      <li>
        <span class="chapter">7.应用案例</span>
        <ul>
          <li @click="goToContent('成功案例')" class="sub-chapter">成功案例</li>
          <li @click="goToContent('失败案例')" class="sub-chapter">失败案例</li>
          <li @click="goToContent('第七章习题')" class="sub-chapter">第七章习题</li>
        </ul>
      </li>

      <!-- 第八章 -->
      <li>
        <span class="chapter">8.实施最佳实践</span>
        <ul>
          <li @click="goToContent('确保需求文档准确完整')" class="sub-chapter">确保需求文档准确完整</li>
          <li @click="goToContent('增加阶段性交付评审')" class="sub-chapter">增加阶段性交付评审</li>
          <li @click="goToContent('使用原型设计减少误解')" class="sub-chapter">使用原型设计减少误解</li>
          <li @click="goToContent('加强文档管理工具')" class="sub-chapter">加强文档管理工具</li>
          <li @click="goToContent('第八章习题')" class="sub-chapter">第八章习题</li>
        </ul>
      </li>

      <!-- 第九章 -->
      <li>
        <span class="chapter">9. 改进方向</span>
        <ul>
          <li @click="goToContent('引入部分敏捷元素')" class="sub-chapter">引入部分敏捷元素</li>
          <li @click="goToContent('增强测试和验证阶段的重视')" class="sub-chapter">增强测试和验证阶段的重视</li>
          <li @click="goToContent('使用持续集成和部署')" class="sub-chapter">使用持续集成和部署</li>
          <li @click="goToContent('第九章习题')" class="sub-chapter">第九章习题</li>
        </ul>
      </li>

      <!-- 第十章 -->
       <li>
        <span class="chapter">10. 总结与反思</span>
        <ul>
          <li @click="goToContent('适用性分析')" class="sub-chapter">适用性分析</li>
          <li @click="goToContent('对传统软件开发的影响')" class="sub-chapter">对传统软件开发的影响</li>
          <li @click="goToContent('面临现代项目的挑战')" class="sub-chapter">面临现代项目的挑战</li>
          <li @click="goToContent('第十章习题')" class="sub-chapter">第十章习题</li>
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
  background-color: #f5f3f3;
  padding: 10px;
  border-right: 2px solid #ddd;
  top: 44px;
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
