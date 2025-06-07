<template>
  <el-card shadow="hover" class="news-article-item-card">
    <template #header>
      <div class="article-header">
        <h3 class="article-title">{{ article.title }}</h3>
      </div>
    </template>
    <div class="article-meta">
      <span v-if="article.source" class="meta-item">
        <el-icon><CollectionTagIcon /></el-icon> Source: {{ article.source }}
      </span>
      <span v-if="article.author" class="meta-item">
        <el-icon><UserIcon /></el-icon> Author: {{ article.author }}
      </span>
      <span class="meta-item">
        <el-icon><ClockIcon /></el-icon> Published: {{ formatDateTime(article.publishedAt) }}
      </span>
      <span v-if="article.postedBy" class="meta-item">
        <el-icon><AvatarIcon /></el-icon> Posted by: {{ article.postedBy }}
      </span>
    </div>
    <div class="article-content-snippet">
      <!-- Simple text snippet. For HTML content, v-html with sanitation would be needed -->
      <p>{{ truncateContent(article.content, 200) }}</p>
    </div>
    <!-- Add a "Read More" link if a detail view is planned -->
    <!-- <router-link :to="{ name: 'NewsArticleDetail', params: { articleId: article.id } }">Read More</router-link> -->
  </el-card>
</template>

<script setup lang="ts">
import { defineProps } from 'vue';
import { ElCard, ElIcon } from 'element-plus';
import { CollectionTag as CollectionTagIcon, User as UserIcon, Clock as ClockIcon, Avatar as AvatarIcon } from '@element-plus/icons-vue';

interface NewsArticleDto {
  id: number;
  title: string;
  content: string;
  source?: string;
  author?: string;
  publishedAt: string;
  postedBy?: string;
}

const props = defineProps<{
  article: NewsArticleDto;
}>();

const formatDateTime = (dateString: string) => {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
};

const truncateContent = (content: string, maxLength: number) => {
  if (!content) return '';
  if (content.length <= maxLength) return content;
  return content.substring(0, maxLength) + '...';
};
</script>

<style scoped>
.news-article-item-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.article-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.article-title {
  margin: 0;
  font-size: 1.4em;
  color: #303133;
}

.article-meta {
  font-size: 0.85em;
  color: #909399;
  margin-bottom: 15px;
  display: flex;
  flex-wrap: wrap; /* Allow items to wrap on smaller screens */
  gap: 15px; /* Spacing between meta items */
}

.meta-item {
  display: flex;
  align-items: center;
}

.meta-item .el-icon {
  margin-right: 5px;
}

.article-content-snippet {
  font-size: 0.95em;
  line-height: 1.7;
  color: #606266;
}
</style>
