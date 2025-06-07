<template>
  <el-container class="forum-home-container">
    <el-aside width="250px" class="topic-aside">
      <TopicList :selected-topic-id="selectedTopicId" @topic-selected="handleTopicSelected" />
      <el-button type="primary" @click="navigateToCreatePost" class="create-post-btn" :icon="EditPenIcon">
        Create Post
      </el-button>
    </el-aside>
    <el-main class="posts-main">
      <div v-if="forumStore.isLoadingPosts" class="loading-posts">
        <el-skeleton :rows="5" animated />
      </div>
      <div v-else-if="forumStore.error && forumStore.posts.length === 0" class="error-posts">
        <el-alert title="Could not load posts" type="error" :description="forumStore.error" show-icon />
      </div>
      <div v-else-if="forumStore.posts.length === 0" class="no-posts">
        <el-empty :description="selectedTopicId ? 'No posts in this topic yet.' : 'No posts available yet.'" />
      </div>
      <div v-else class="post-list">
        <PostItem v-for="post in forumStore.posts" :key="post.id" :post="post" />
        <el-pagination
          v-if="forumStore.pagination.totalPages > 1"
          background
          layout="prev, pager, next, jumper, ->, total"
          :total="forumStore.pagination.totalItems"
          :page-size="forumStore.pagination.pageSize"
          :current-page="forumStore.pagination.currentPage + 1"
          @current-change="handlePageChange"
          class="pagination-controls"
        />
      </div>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useForumStore } from '../store/forum';
import TopicList from '../components/forum/TopicList.vue';
import PostItem from '../components/forum/PostItem.vue';
import { ElContainer, ElAside, ElMain, ElPagination, ElButton, ElSkeleton, ElAlert, ElEmpty, ElMessage } from 'element-plus';
import { EditPen as EditPenIcon } from '@element-plus/icons-vue';
import { useAuthStore } from '../store/auth';


const forumStore = useForumStore();
const authStore = useAuthStore();
const router = useRouter();

const selectedTopicId = ref<number | null>(null);

const loadPosts = (page?: number, topicId?: number | null) => {
  const currentPage = page !== undefined ? page : forumStore.pagination.currentPage;
  const currentTopicId = topicId !== undefined ? topicId : selectedTopicId.value;
  forumStore.fetchPosts(currentPage, forumStore.pagination.pageSize, currentTopicId || undefined);
};

onMounted(() => {
  loadPosts(0, null); // Load all posts on initial mount
});

watch(selectedTopicId, (newTopicId) => {
  loadPosts(0, newTopicId); // Reset to page 0 when topic changes
});

const handleTopicSelected = (topicId: number | null) => {
  selectedTopicId.value = topicId;
  // The watcher will trigger loadPosts
};

const handlePageChange = (newPage: number) => {
  // ElPagination's current-change event gives 1-based page number
  loadPosts(newPage - 1, selectedTopicId.value);
};

const navigateToCreatePost = () => {
  if (!authStore.isAuthenticated) {
    ElMessage.warning('Please login to create a post.');
    router.push({ name: 'Login', query: { redirect: router.currentRoute.value.fullPath } });
    return;
  }
  // Placeholder: Navigate to create post view (to be implemented in a future task)
  ElMessage.info('Create Post page is not yet implemented.');
  // router.push({ name: 'CreatePost' }); // Assuming a route named 'CreatePost'
};

</script>

<style scoped>
.forum-home-container {
  padding: 20px;
  background-color: #f4f6f8; /* Light background for the page */
}

.topic-aside {
  padding-right: 20px;
  background-color: #fff; /* White background for the aside */
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
  height: fit-content; /* Make aside height fit content */
}

.create-post-btn {
  width: calc(100% - 40px); /* Adjust width considering padding */
  margin: 20px; /* Add margin around the button */
}

.posts-main {
  /* No specific background needed if page background is set */
}

.loading-posts, .error-posts, .no-posts {
  padding: 40px;
  text-align: center;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
}

.post-list {
  /* No specific styling needed here if PostItem handles its own card styling */
}

.pagination-controls {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  padding: 10px 0;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
}
</style>
