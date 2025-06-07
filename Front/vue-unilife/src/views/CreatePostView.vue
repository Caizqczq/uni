<template>
  <div class="create-post-container">
    <el-page-header @back="goBack" content="Create New Post" class="page-header" />
    <PostForm
      :is-edit-mode="false"
      :is-loading="isLoading"
      @submit-form="handleCreatePost"
      @cancel="goBack"
    />
    <div v-if="forumStore.error" class="error-message-global">
      <el-alert :title="forumStore.error" type="error" show-icon :closable="false" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useForumStore } from '../store/forum';
import PostForm from '../components/forum/PostForm.vue';
import { ElMessage, ElPageHeader, ElAlert } from 'element-plus';

interface PostFormData {
  title: string;
  content: string;
  topicId: number;
}

const router = useRouter();
const forumStore = useForumStore();
const isLoading = ref(false);

const handleCreatePost = async (formData: PostFormData) => {
  isLoading.value = true;
  forumStore.error = null; // Clear previous errors
  try {
    const newPost = await forumStore.createPost(formData);
    if (newPost && newPost.id) {
      ElMessage.success('Post created successfully!');
      router.push({ name: 'PostDetail', params: { postId: newPost.id.toString() } });
    } else if (!forumStore.error) {
      // Fallback if no specific error but post not returned as expected
      ElMessage.error('Failed to create post. Unexpected response.');
    }
    // If forumStore.error is set, the template will display it
  } catch (error) {
    // Error should be set in the store by the action, template displays it.
    // If not, ElMessage.error('An unexpected error occurred.');
    // console.error("Create post error in view:", error);
  } finally {
    isLoading.value = false;
  }
};

const goBack = () => {
  router.push('/forum'); // Or router.go(-1)
};
</script>

<style scoped>
.create-post-container {
  max-width: 900px;
  margin: 20px auto;
  padding: 20px;
}
.page-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}
.error-message-global {
    margin-top: 20px;
}
</style>
