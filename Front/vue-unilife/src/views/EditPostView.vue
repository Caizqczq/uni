<template>
  <div class="edit-post-container">
    <el-page-header @back="goBack" content="Edit Post" class="page-header" />
    <div v-if="isLoadingPostData" class="loading-section">
      <el-skeleton :rows="6" animated />
    </div>
    <div v-else-if="!canEditPost" class="error-section">
      <el-alert
        title="Permission Denied"
        type="error"
        description="You do not have permission to edit this post, or the post was not found."
        show-icon
        :closable="false"
      />
      <el-button @click="goBackToForum" style="margin-top: 20px;">Back to Forum</el-button>
    </div>
    <PostForm
      v-else
      :is-edit-mode="true"
      :initial-data="postDataToEdit"
      :is-loading="isSubmitting"
      @submit-form="handleUpdatePost"
      @cancel="goBackToPost"
    />
    <div v-if="forumStore.error && !isLoadingPostData" class="error-message-global">
      <el-alert :title="forumStore.error" type="error" show-icon :closable="false" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useForumStore } from '../store/forum';
import { useAuthStore } from '../store/auth'; // For checking ownership
import PostForm from '../components/forum/PostForm.vue';
import { ElMessage, ElPageHeader, ElAlert, ElSkeleton, ElButton } from 'element-plus';

interface PostFormData {
  title: string;
  content: string;
  topicId: number;
}

interface PostDataForEdit {
  title: string;
  content: string;
  topicId: number | null; // Can be null if topic not found or during loading
}

const route = useRoute();
const router = useRouter();
const forumStore = useForumStore();
const authStore = useAuthStore();

const postId = route.params.postId as string;
const postDataToEdit = ref<PostDataForEdit | null>(null);
const isLoadingPostData = ref(true);
const isSubmitting = ref(false);
const canEditPost = ref(false);


const loadPostForEditing = async () => {
  isLoadingPostData.value = true;
  forumStore.error = null;
  try {
    await forumStore.fetchPostById(postId);
    if (forumStore.currentPost) {
      // Basic ownership check (backend will enforce definitively)
      if (authStore.user && authStore.user.username === forumStore.currentPost.authorUsername) {
        postDataToEdit.value = {
          title: forumStore.currentPost.title,
          content: forumStore.currentPost.content,
          topicId: forumStore.currentPost.topicId, // Ensure your DTO has topicId
        };
        canEditPost.value = true;
      } else {
        canEditPost.value = false;
        ElMessage.error('You are not authorized to edit this post.');
        // Consider redirecting or showing a more prominent error message
      }
    } else if (forumStore.error) {
        canEditPost.value = false;
        ElMessage.error('Failed to load post data: ' + forumStore.error);
    } else {
        canEditPost.value = false; // Post not found
        ElMessage.error('Post not found.');
    }
  } catch (error) {
    canEditPost.value = false;
    // Error should be set in store, or handle here
    // ElMessage.error('Failed to load post for editing.');
    console.error("Error loading post for edit:", error);
  } finally {
    isLoadingPostData.value = false;
  }
};

onMounted(loadPostForEditing);

// Watch for changes if postId in route changes (e.g., navigating between edit pages directly)
watch(() => route.params.postId, (newPostId) => {
  if (newPostId && newPostId !== postId) {
    loadPostForEditing();
  }
});


const handleUpdatePost = async (formData: PostFormData) => {
  isSubmitting.value = true;
  forumStore.error = null;
  try {
    const updatedPost = await forumStore.updatePost(postId, formData);
    if (updatedPost && updatedPost.id) {
      ElMessage.success('Post updated successfully!');
      router.push({ name: 'PostDetail', params: { postId: updatedPost.id.toString() } });
    } else if (!forumStore.error) {
       ElMessage.error('Failed to update post. Unexpected response.');
    }
  } catch (error) {
    // Error should be set in store
    // ElMessage.error('Failed to update post.');
  } finally {
    isSubmitting.value = false;
  }
};

const goBack = () => {
  router.go(-1); // Go back to previous page
};

const goBackToPost = () => {
  router.push({ name: 'PostDetail', params: { postId } });
};
const goBackToForum = () => {
  router.push({ name: 'ForumHome' });
}
</script>

<style scoped>
.edit-post-container {
  max-width: 900px;
  margin: 20px auto;
  padding: 20px;
}
.page-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}
.loading-section, .error-section {
  padding: 40px;
  text-align: center;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
  margin-bottom: 20px;
}
.error-message-global {
    margin-top: 20px;
}
</style>
