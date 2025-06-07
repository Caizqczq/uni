<template>
  <el-card shadow="hover" class="document-list-item-card" @click="navigateToDetail">
    <div class="item-content">
      <div class="item-header">
        <h4 class="document-title">{{ document.title }}</h4>
        <el-tag v-if="document.courseInfo" size="small" type="info" class="course-tag">
          {{ document.courseInfo.courseCode }} - {{ document.courseInfo.courseName }}
        </el-tag>
      </div>
      <div class="document-meta">
        <span class="meta-item" v-if="document.lastUpdatedByUsername">
          <el-icon><UserIcon /></el-icon> Last updated by: {{ document.lastUpdatedByUsername }}
        </span>
        <span class="meta-item">
          <el-icon><ClockIcon /></el-icon> Updated: {{ timeAgo(document.updatedAt) }}
        </span>
        <span class="meta-item">
          <el-icon><CollectionTagIcon /></el-icon> Version: {{ document.version }}
        </span>
      </div>
      <!-- Optional: Snippet of content -->
      <!-- <p class="content-snippet">{{ truncateContent(document.content, 100) }}</p> -->
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { defineProps } from 'vue';
import { useRouter } from 'vue-router';
import { ElCard, ElTag, ElIcon } from 'element-plus';
import { User as UserIcon, Clock as ClockIcon, CollectionTag as CollectionTagIcon } from '@element-plus/icons-vue';

interface CourseInfoDto {
  id: number;
  courseCode: string;
  courseName: string;
}

interface SharedDocumentResponseDto {
  id: number;
  title: string;
  content: string; // For potential snippet
  courseInfo?: CourseInfoDto;
  lastUpdatedByUsername?: string;
  updatedAt: string;
  version: number;
}

const props = defineProps<{
  document: SharedDocumentResponseDto;
}>();

const router = useRouter();

const navigateToDetail = () => {
  router.push({ name: 'DocumentDetail', params: { docId: props.document.id.toString() } });
};

const timeAgo = (dateString: string) => {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  const now = new Date();
  const seconds = Math.round((now.getTime() - date.getTime()) / 1000);
  const minutes = Math.round(seconds / 60);
  const hours = Math.round(minutes / 60);
  const days = Math.round(hours / 24);

  if (seconds < 60) return `${seconds} sec ago`;
  if (minutes < 60) return `${minutes} min ago`;
  if (hours < 24) return `${hours} hr ago`;
  if (days < 7) return `${days} day(s) ago`;
  return date.toLocaleDateString();
};

// const truncateContent = (content: string, maxLength: number) => {
//   if (!content) return '';
//   if (content.length <= maxLength) return content;
//   return content.substring(0, maxLength) + '...';
// };
</script>

<style scoped>
.document-list-item-card {
  margin-bottom: 15px;
  cursor: pointer;
  border-radius: 6px;
  transition: box-shadow 0.3s ease-in-out;
}

.document-list-item-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.item-content {
  display: flex;
  flex-direction: column;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.document-title {
  margin: 0;
  font-size: 1.15em;
  color: #303133;
  font-weight: 500;
}

.course-tag {
  margin-left: 10px;
  flex-shrink: 0;
}

.document-meta {
  font-size: 0.85em;
  color: #909399;
  display: flex;
  flex-wrap: wrap;
  gap: 12px; /* Spacing between meta items */
}

.meta-item {
  display: flex;
  align-items: center;
}

.meta-item .el-icon {
  margin-right: 4px;
}

.content-snippet {
  font-size: 0.9em;
  color: #606266;
  margin-top: 8px;
  line-height: 1.5;
}
</style>
