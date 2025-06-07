import { defineStore } from 'pinia';
import axios from 'axios';
import { ref, Ref } from 'vue';

// Assuming NewsArticleDto is defined (adjust if needed)
interface NewsArticleDto {
  id: number;
  title: string;
  content: string;
  source?: string;
  author?: string;
  publishedAt: string; // Assuming ISO string date
  createdAt: string;   // Assuming ISO string date
  updatedAt: string;   // Assuming ISO string date
  postedBy?: string;  // Username or nickname of poster
}

interface PaginationData {
  currentPage: number;
  pageSize: number;
  totalItems: number;
  totalPages: number;
}

export const useNewsStore = defineStore('news', () => {
  // State
  const newsArticles: Ref<NewsArticleDto[]> = ref([]);
  const currentNewsArticle: Ref<NewsArticleDto | null> = ref(null);
  const isLoadingNews: Ref<boolean> = ref(false);
  const error: Ref<string | null> = ref(null);
  const pagination: Ref<PaginationData> = ref({
    currentPage: 0,
    pageSize: 10, // Default page size
    totalItems: 0,
    totalPages: 0,
  });

  // Actions
  async function fetchNewsArticles(page: number = 0, size: number = pagination.value.pageSize) {
    isLoadingNews.value = true;
    error.value = null;
    try {
      const response = await axios.get<any>('/api/news', { // Adjust endpoint if needed
        params: { page, size },
      });
      // Assuming backend returns a PageResponse-like structure
      newsArticles.value = response.data.content || [];
      pagination.value = {
        currentPage: response.data.pageNumber ?? response.data.number ?? page,
        pageSize: response.data.pageSize ?? response.data.size ?? size,
        totalItems: response.data.totalElements ?? 0,
        totalPages: response.data.totalPages ?? 0,
      };
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || 'Failed to fetch news articles.';
      newsArticles.value = []; // Clear articles on error
    } finally {
      isLoadingNews.value = false;
    }
  }

  async function fetchNewsArticleById(articleId: string | number) {
    isLoadingNews.value = true; // Can use a more specific loader like isLoadingCurrentNewsArticle
    error.value = null;
    currentNewsArticle.value = null;
    try {
      const response = await axios.get<NewsArticleDto>(`/api/news/${articleId}`);
      currentNewsArticle.value = response.data;
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || `Failed to fetch news article ${articleId}.`;
    } finally {
      isLoadingNews.value = false;
    }
  }

  return {
    newsArticles,
    currentNewsArticle,
    isLoadingNews,
    error,
    pagination,
    fetchNewsArticles,
    fetchNewsArticleById,
  };
});
