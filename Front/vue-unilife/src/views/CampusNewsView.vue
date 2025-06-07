<template>
  <el-container class="campus-news-container">
    <el-header class="page-header-container">
      <h1>Campus News</h1>
    </el-header>
    <el-main>
      <div v-if="newsStore.isLoadingNews && newsStore.newsArticles.length === 0" class="loading-section">
        <el-skeleton :rows="5" animated />
      </div>
      <div v-else-if="newsStore.error && newsStore.newsArticles.length === 0" class="error-section">
        <el-alert title="Could not load news articles" type="error" :description="newsStore.error" show-icon />
      </div>
      <div v-else-if="newsStore.newsArticles.length === 0" class="no-articles">
        <el-empty description="No news articles available at the moment." />
      </div>
      <div v-else class="news-list">
        <NewsArticleItem
          v-for="article in newsStore.newsArticles"
          :key="article.id"
          :article="article"
        />
        <el-pagination
          v-if="newsStore.pagination.totalPages > 1"
          background
          layout="prev, pager, next, jumper, ->, total"
          :total="newsStore.pagination.totalItems"
          :page-size="newsStore.pagination.pageSize"
          :current-page="newsStore.pagination.currentPage + 1"
          @current-change="handlePageChange"
          class="pagination-controls"
        />
      </div>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useNewsStore } from '../store/news';
import NewsArticleItem from '../components/news/NewsArticleItem.vue';
import { ElContainer, ElHeader, ElMain, ElPagination, ElSkeleton, ElAlert, ElEmpty } from 'element-plus';

const newsStore = useNewsStore();

const loadNews = (page?: number) => {
  const currentPage = page !== undefined ? page : newsStore.pagination.currentPage;
  newsStore.fetchNewsArticles(currentPage, newsStore.pagination.pageSize);
};

onMounted(() => {
  // Fetch initial set of articles if not already loaded or if pagination needs reset
  if (newsStore.newsArticles.length === 0 || newsStore.pagination.currentPage !== 0) {
     loadNews(0);
  }
});

const handlePageChange = (newPage: number) => {
  // ElPagination's current-change event gives 1-based page number
  loadNews(newPage - 1);
};
</script>

<style scoped>
.campus-news-container {
  padding: 20px;
  background-color: #f4f6f8; /* Light background for the page */
}

.page-header-container {
  display: flex;
  align-items: center;
  justify-content: space-between; /* For potential future elements like create button */
  padding: 0; /* Reset padding if ElHeader adds it */
  margin-bottom: 20px;
  height: auto; /* Adjust height to content */
}

.page-header-container h1 {
  margin: 0; /* Reset margin if ElHeader adds it */
  font-size: 2em;
  color: #303133;
}

.loading-section, .error-section, .no-articles {
  padding: 40px;
  text-align: center;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
}

.news-list {
  /* No specific styling needed here if NewsArticleItem handles its own card styling */
}

.pagination-controls {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  padding: 10px 0;
  background-color: #fff; /* Optional: background for pagination area */
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05); /* Optional shadow */
}
</style>
