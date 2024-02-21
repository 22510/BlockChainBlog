<template>
  <div class="blog">
    <el-card class="box-card" shadow="hover" v-for="article in articles" :key="article">
      <template #header>
        <div class="card-header">
          <h2>{{ article.title }}</h2>
          <p>作者: {{ article.author }}</p>
          <p>创建时间: {{ article.createdAt }}</p>
          <p>点赞数: {{ article.likes }}</p>
          <p>评论数: {{ article.comments }}</p>
          <p>标签: {{ article.tags}}</p>
          <el-button class="button" text>查看详情</el-button>
        </div>
      </template>
      <div v-for="o in 4" :key="o" class="text item">{{ 'List item ' + o }}</div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";

const article = ref([
{
          id: 1,
          title: "文章标题1",
          author: "作者1",
          createdAt: "2024-02-16",
          likes: 10,
          comments: 5,
          tags: ["标签1", "标签2"]
        }
]);

const articles = ref();

</script>

<style scoped>
.blog {
  width: 85%;
  margin: 0 auto;
  /* 居中状态 */
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  /* width: 480px; */
  margin-top: 15px;
  margin-bottom: 15px;
}
</style>