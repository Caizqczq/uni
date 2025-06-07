<template>
  <el-card shadow="hover" class="post-item-card">
    <template #header>
      <div class="post-header">
        <router-link :to="{ name: 'PostDetail', params: { postId: post.id } }" class="post-title-link">
          <h3>{{ post.title }}</h3>
        </router-link>
        <el-tag size="small" type="info">{{ post.topicName }}</el-tag>
      </div>
    </template>
    <div class="post-meta">
      <el-avatar :size="30" :src="post.authorAvatarUrl" class="author-avatar">
        <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/> <!-- Default -->
      </el-avatar>
      <span class="author-name">{{ post.authorNickname || post.authorUsername }}</span>
      <span class="post-date">
        <el-icon><Clock /></el-icon>
        {{ timeAgo(post.createdAt) }}
      </span>
    </div>
    <!-- Simple content preview - could be truncated or a summary -->
    <div class="post-content-preview" @click="navigateToPost">
       {{ truncateContent(post.content, 150) }}
    </div>
    <el-divider />
    <div class="post-actions">
      <el-button
        :type="isLiked ? 'danger' : 'default'"
        plain
        size="small"
        @click.stop="toggleLike"
        :icon="StarIcon"
        :loading="isLiking"
      >
        {{ post.likesCount }} {{ isLiked ? 'Unlike' : 'Like' }}
      </el-button>
      <el-button type="default" plain size="small" :icon="ChatDotRoundIcon" @click="navigateToPost">
        {{ commentsCount }} Comments
      </el-button>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { defineProps, ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useForumStore } from '../../store/forum';
import { useAuthStore } from '../../store/auth'; // To check if user is authenticated for like/unlike
import { ElCard, ElTag, ElAvatar, ElButton, ElIcon, ElDivider, ElMessage } from 'element-plus';
import { Star as StarIcon, ChatDotRound as ChatDotRoundIcon, Clock } from '@element-plus/icons-vue';

interface PostResponseDto {
  id: number;
  title: string;
  content: string;
  authorUsername: string;
  authorNickname?: string;
  authorAvatarUrl?: string;
  topicName: string;
  topicId: number;
  createdAt: string;
  updatedAt: string;
  likesCount: number;
  // commentsCount?: number; // Assuming this might be added to DTO later
}

const props = defineProps<{
  post: PostResponseDto;
}>();

const forumStore = useForumStore();
const authStore = useAuthStore();
const router = useRouter();

const isLiking = ref(false);
// Placeholder for actual liked status (would come from user-specific data or API)
const isLiked = ref(false); // In a real app, this needs to be fetched or stored per user
const commentsCount = ref(0); // Placeholder, fetch if not on DTO

// Simulate fetching comments count or liked status if not directly on post DTO
// onMounted(() => {
//   // e.g., fetch comment count for props.post.id if not available
//   // or check if current user has liked this post
// });

const toggleLike = async () => {
  if (!authStore.isAuthenticated) {
    ElMessage.warning('Please login to like posts.');
    router.push('/login');
    return;
  }
  isLiking.value = true;
  try {
    if (isLiked.value) { // This local isLiked is a placeholder
      await forumStore.unlikePost(props.post.id);
      isLiked.value = false; // Update local placeholder
    } else {
      await forumStore.likePost(props.post.id);
      isLiked.value = true; // Update local placeholder
    }
    // Note: The store action already updates the post's likesCount in the main posts array
    // If it didn't, you might need to do:
    // props.post.likesCount = isLiked.value ? props.post.likesCount + 1 : props.post.likesCount - 1;
  } catch (error) {
    ElMessage.error(forumStore.error || 'Action failed.');
  } finally {
    isLiking.value = false;
  }
};

const navigateToPost = () => {
  router.push({ name: 'PostDetail', params: { postId: props.post.id } });
};

const truncateContent = (content: string, maxLength: number) => {
  if (!content) return '';
  if (content.length <= maxLength) return content;
  return content.substring(0, maxLength) + '...';
};

const timeAgo = (dateString: string) => {
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

</script>

<style scoped>
.post-item-card {
  margin-bottom: 20px;
  border-radius: 8px;
}
.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.post-title-link {
  text-decoration: none;
  color: #303133; /* ElCard header color */
}
.post-title-link:hover h3 {
  color: #409EFF; /* Element Plus primary color */
}
.post-meta {
  display: flex;
  align-items: center;
  font-size: 0.9em;
  color: #909399;
  margin-bottom: 15px;
}
.author-avatar {
  margin-right: 8px;
}
.author-name {
  font-weight: 500;
  margin-right: 15px;
}
.post-date {
  display: flex;
  align-items: center;
}
.post-date .el-icon {
  margin-right: 4px;
}
.post-content-preview {
  font-size: 0.95em;
  line-height: 1.6;
  color: #606266;
  margin-bottom: 15px;
  cursor: pointer;
}
.post-actions {
  display: flex;
  justify-content: flex-start;
  gap: 10px;
}
</style>
