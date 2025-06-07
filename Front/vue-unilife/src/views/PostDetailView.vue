<template>
  <el-container class="post-detail-container">
    <el-main>
      <el-breadcrumb separator-class="el-icon-arrow-right" class="breadcrumb-nav">
        <el-breadcrumb-item :to="{ path: '/forum' }">Forum</el-breadcrumb-item>
        <el-breadcrumb-item v-if="forumStore.currentPost?.topicName" :to="{ path: '/forum', query: { topicId: forumStore.currentPost?.topicId }}">{{ forumStore.currentPost?.topicName }}</el-breadcrumb-item>
        <el-breadcrumb-item>{{ postTitleTruncated }}</el-breadcrumb-item>
      </el-breadcrumb>

      <div v-if="forumStore.isLoadingCurrentPost" class="loading-section">
        <el-skeleton :rows="8" animated />
      </div>
      <div v-else-if="forumStore.error && !forumStore.currentPost" class="error-section">
        <el-alert title="Error loading post" type="error" :description="forumStore.error" show-icon />
        <el-button @click="goBackToForum" style="margin-top: 20px;">Back to Forum</el-button>
      </div>
      <el-card v-else-if="forumStore.currentPost" class="post-content-card">
        <template #header>
          <div class="post-header-detail">
            <h1>{{ forumStore.currentPost.title }}</h1>
            <div class="post-meta-detail">
              <el-avatar :size="40" :src="forumStore.currentPost.authorAvatarUrl" class="author-avatar-detail">
                 <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/>
              </el-avatar>
              <span class="author-name-detail">{{ forumStore.currentPost.authorNickname || forumStore.currentPost.authorUsername }}</span>
              <span class="post-date-detail">
                <el-icon><Clock /></el-icon>
                Posted: {{ formatDateTime(forumStore.currentPost.createdAt) }}
                (Updated: {{ formatDateTime(forumStore.currentPost.updatedAt) }})
              </span>
            </div>
          </div>
        </template>
        <div class="post-full-content" v-html="renderMarkdown(forumStore.currentPost.content)"></div>
        <el-divider />
        <div class="post-actions-detail">
          <el-button
            :type="isLiked ? 'danger' : 'default'"
            plain
            @click.stop="toggleLike"
            :icon="StarIcon"
            :loading="isLiking"
          >
            {{ forumStore.currentPost.likesCount }} {{ isLiked ? 'Unlike' : 'Like' }}
          </el-button>
        </div>
      </el-card>

      <el-card class="comments-section" v-if="forumStore.currentPost">
        <template #header>
          <h3>Comments ({{ forumStore.comments.length }})</h3>
        </template>
        <div v-if="forumStore.isLoadingComments">
          <el-skeleton :rows="3" animated />
        </div>
        <div v-else-if="forumStore.error && forumStore.comments.length === 0" class="error-comments">
           <el-alert title="Could not load comments" type="error" :description="forumStore.error" show-icon />
        </div>
        <div v-else-if="forumStore.comments.length === 0" class="no-comments">
          <el-empty description="No comments yet. Be the first to comment!" />
        </div>
        <div v-else class="comment-list">
          <div v-for="comment in forumStore.comments" :key="comment.id" class="comment-item">
            <el-avatar :size="30" :src="comment.authorAvatarUrl" class="comment-avatar">
               <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/>
            </el-avatar>
            <div class="comment-content">
              <span class="comment-author">{{ comment.authorNickname || comment.authorUsername }}</span>
              <span class="comment-date">{{ timeAgo(comment.createdAt) }}</span>
              <p class="comment-text" v-html="renderMarkdown(comment.content)"></p>
            </div>
          </div>
        </div>
        <el-divider />
        <h4>Add a Comment</h4>
        <div v-if="authStore.isAuthenticated">
          <el-input
            type="textarea"
            :rows="3"
            placeholder="Write your comment..."
            v-model="newCommentContent"
            class="comment-input"
          />
          <el-button type="primary" @click="submitComment" :loading="isSubmittingComment" style="margin-top: 10px;">
            Submit Comment
          </el-button>
           <div v-if="commentError" class="error-message comment-error-message">
             <el-alert :title="commentError" type="error" show-icon :closable="false" />
           </div>
        </div>
        <div v-else>
          <el-alert type="info" show-icon :closable="false">
            Please <router-link to="/login">login</router-link> to add a comment.
          </el-alert>
        </div>
      </el-card>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useForumStore } from '../store/forum';
import { useAuthStore } from '../store/auth';
import { ElContainer, ElMain, ElCard, ElButton, ElAvatar, ElIcon, ElDivider, ElSkeleton, ElInput, ElMessage, ElAlert, ElEmpty, ElBreadcrumb, ElBreadcrumbItem } from 'element-plus';
import { Star as StarIcon, Clock } from '@element-plus/icons-vue';
import DOMPurify from 'dompurify';
import { marked } from 'marked'; // For Markdown rendering

const route = useRoute();
const router = useRouter();
const forumStore = useForumStore();
const authStore = useAuthStore();

const postId = ref(route.params.postId as string);
const newCommentContent = ref('');
const isLiking = ref(false);
const isLiked = ref(false); // Placeholder, should be derived from user-specific data
const isSubmittingComment = ref(false);
const commentError = ref<string | null>(null);

const postTitleTruncated = computed(() => {
  const title = forumStore.currentPost?.title;
  if (!title) return 'Post';
  return title.length > 30 ? title.substring(0, 30) + '...' : title;
});


const fetchData = async () => {
  await forumStore.fetchPostById(postId.value);
  if (forumStore.currentPost) {
    await forumStore.fetchCommentsByPostId(postId.value);
    // Placeholder: check if current user liked this post
    // e.g., isLiked.value = checkUserLikeStatus(forumStore.currentPost.id, authStore.user?.id);
  }
};

onMounted(fetchData);

watch(() => route.params.postId, (newPostId) => {
  if (newPostId && newPostId !== postId.value) {
    postId.value = newPostId as string;
    fetchData();
  }
});

const toggleLike = async () => {
  if (!authStore.isAuthenticated) {
    ElMessage.warning('Please login to like posts.');
    router.push('/login');
    return;
  }
  if (!forumStore.currentPost) return;

  isLiking.value = true;
  try {
    if (isLiked.value) { // This local isLiked is a placeholder
      await forumStore.unlikePost(forumStore.currentPost.id);
      isLiked.value = false;
    } else {
      await forumStore.likePost(forumStore.currentPost.id);
      isLiked.value = true;
    }
  } catch (error) {
    ElMessage.error(forumStore.error || 'Action failed.');
  } finally {
    isLiking.value = false;
  }
};

const submitComment = async () => {
  if (!newCommentContent.value.trim()) {
    ElMessage.warning('Comment cannot be empty.');
    return;
  }
  if (!forumStore.currentPost) return;

  isSubmittingComment.value = true;
  commentError.value = null;
  try {
    await forumStore.createComment({
      postId: forumStore.currentPost.id,
      content: newCommentContent.value,
    });
    newCommentContent.value = ''; // Clear input
    ElMessage.success('Comment posted successfully!');
    // Optionally refetch comments, or the store action might optimistically update
    await forumStore.fetchCommentsByPostId(postId.value);
  } catch (err: any) {
    commentError.value = forumStore.error || 'Failed to post comment.';
    // ElMessage.error(forumStore.error || 'Failed to post comment.');
  } finally {
    isSubmittingComment.value = false;
  }
};

const formatDateTime = (dateString: string) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleString();
};

const timeAgo = (dateString: string) => {
  if (!dateString) return 'just now';
  const date = new Date(dateString);
  const now = new Date();
  const seconds = Math.round((now.getTime() - date.getTime()) / 1000);
  const minutes = Math.round(seconds / 60);
  const hours = Math.round(minutes / 60);
  const days = Math.round(hours / 24);

  if (seconds < 60) return `${seconds} sec ago`;
  if (minutes < 60) return `${minutes} min ago`;
  if (hours < 24) return `${hours} hr ago`;
  return `${days} days ago`;
};

const renderMarkdown = (content: string) => {
  if (!content) return '';
  // Ensure DOMPurify is used if content can be arbitrary HTML/Markdown from users
  return DOMPurify.sanitize(marked(content) as string);
};

const goBackToForum = () => {
  router.push('/forum');
}
</script>

<style scoped>
.post-detail-container {
  padding: 20px;
  background-color: #f4f6f8;
}
.breadcrumb-nav {
  margin-bottom: 20px;
  background-color: #fff;
  padding: 15px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
}
.loading-section, .error-section {
  padding: 40px;
  text-align: center;
  background-color: #fff;
  border-radius: 8px;
}
.post-content-card {
  margin-bottom: 20px;
  border-radius: 8px;
}
.post-header-detail h1 {
  margin-bottom: 10px;
  font-size: 2em;
}
.post-meta-detail {
  display: flex;
  align-items: center;
  font-size: 0.9em;
  color: #909399;
  margin-bottom: 20px;
}
.author-avatar-detail {
  margin-right: 10px;
}
.author-name-detail {
  font-weight: 500;
  margin-right: 20px;
}
.post-date-detail .el-icon {
  margin-right: 5px;
}
.post-full-content {
  line-height: 1.8;
  font-size: 1.05em;
  word-wrap: break-word;
}
.post-full-content :deep(p) { /* Example for styling parsed markdown */
  margin-bottom: 1em;
}
.post-full-content :deep(h2) {
  margin-top: 1.5em;
  margin-bottom: 0.5em;
  border-bottom: 1px solid #eee;
  padding-bottom: 0.3em;
}
.post-actions-detail {
  margin-top: 15px;
}
.comments-section {
  margin-top: 20px;
  border-radius: 8px;
}
.comment-list {
  margin-top: 10px;
}
.comment-item {
  display: flex;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}
.comment-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}
.comment-avatar {
  margin-right: 12px;
  flex-shrink: 0; /* Prevent avatar from shrinking */
}
.comment-content {
  flex-grow: 1;
}
.comment-author {
  font-weight: bold;
  margin-right: 8px;
  font-size: 0.95em;
}
.comment-date {
  font-size: 0.8em;
  color: #909399;
}
.comment-text {
  margin-top: 5px;
  font-size: 0.95em;
  line-height: 1.7;
  color: #303133;
  white-space: pre-wrap; /* Preserve whitespace and newlines */
}
.comment-input {
  margin-bottom: 10px;
}
.error-message.comment-error-message {
  margin-top: 10px;
}
</style>
