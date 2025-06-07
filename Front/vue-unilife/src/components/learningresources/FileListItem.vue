<template>
  <el-card shadow="hover" class="file-list-item-card">
    <div class="item-content">
      <div class="item-header">
        <el-icon :size="24" class="file-icon"><DocumentIcon /></el-icon>
        <h4 class="file-name">{{ file.fileName }}</h4>
        <el-tag v-if="file.courseInfo" size="small" type="info" class="course-tag">
          {{ file.courseInfo.courseCode }}
        </el-tag>
      </div>
      <div class="file-meta">
        <span class="meta-item" title="File Type">
          <el-icon><PriceTagIcon /></el-icon> {{ file.fileType }}
        </span>
        <span class="meta-item" title="File Size">
          <el-icon><CoinIcon /></el-icon> {{ formatFileSize(file.fileSize) }}
        </span>
        <span class="meta-item" v-if="file.uploadedByUsername" title="Uploader">
          <el-icon><UserIcon /></el-icon> {{ file.uploadedByUsername }}
        </span>
        <span class="meta-item" title="Upload Date">
          <el-icon><ClockIcon /></el-icon> {{ timeAgo(file.createdAt) }}
        </span>
      </div>
      <p v-if="file.description" class="file-description">{{ file.description }}</p>
      <div class="item-actions">
        <el-button type="primary" plain size="small" :icon="DownloadIcon" @click="handleDownload">
          Download
        </el-button>
        <el-popconfirm
          v-if="canDelete"
          title="Are you sure you want to delete this file?"
          confirm-button-text="Yes, Delete"
          cancel-button-text="No"
          @confirm="handleDelete"
        >
          <template #reference>
            <el-button type="danger" plain size="small" :icon="DeleteIcon" :loading="isDeleting">
              Delete
            </el-button>
          </template>
        </el-popconfirm>
      </div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { defineProps, defineEmits, computed, ref } from 'vue';
import { useAuthStore } from '../../store/auth';
import { ElCard, ElTag, ElIcon, ElButton, ElPopconfirm, ElMessage } from 'element-plus';
import {
  Document as DocumentIcon,
  User as UserIcon,
  Clock as ClockIcon,
  Download as DownloadIcon,
  Delete as DeleteIcon,
  PriceTag as PriceTagIcon, // For file type
  Coin as CoinIcon // For file size
} from '@element-plus/icons-vue';

interface CourseInfoDto {
  id: number;
  courseCode: string;
  courseName: string;
}

interface SharedFileResponseDto {
  id: number;
  fileName: string;
  fileType: string;
  fileSize: number; // Assuming long is serialized as number
  courseInfo?: CourseInfoDto | null;
  uploadedByUsername?: string;
  createdAt: string;
  description?: string;
}

const props = defineProps<{
  file: SharedFileResponseDto;
}>();

const emit = defineEmits(['delete-file']);

const authStore = useAuthStore();
const isDeleting = ref(false);

const canDelete = computed(() => {
  if (!authStore.isAuthenticated || !authStore.user) return false;
  // Add admin role check if needed: || authStore.user.roles.includes('ADMIN')
  return authStore.user.username === props.file.uploadedByUsername;
});

const handleDownload = () => {
  // Construct the download URL. Ensure your API serves files appropriately.
  // This might require an API token if downloads are protected beyond simple GET.
  // For now, assume a direct GET link.
  const downloadUrl = `/api/files/${props.file.id}/download`; // Adjust if your API differs
  window.open(downloadUrl, '_blank');
};

const handleDelete = async () => {
  isDeleting.value = true;
  try {
    emit('delete-file', props.file.id);
    // Success message can be handled by parent if needed, or here
    // ElMessage.success('File delete request sent.');
  } catch (error) {
    // Error message handled by parent or store
    // ElMessage.error('Failed to initiate delete.');
  } finally {
    // isDeleting.value = false; // Parent might control overall loading state
  }
};

const formatFileSize = (bytes: number, decimals = 2) => {
  if (bytes === 0) return '0 Bytes';
  const k = 1024;
  const dm = decimals < 0 ? 0 : decimals;
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
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
</script>

<style scoped>
.file-list-item-card {
  margin-bottom: 15px;
  border-radius: 6px;
}

.item-content {
  display: flex;
  flex-direction: column;
}

.item-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.file-icon {
  margin-right: 10px;
  color: #409EFF; /* Element Plus primary color */
}

.file-name {
  margin: 0;
  font-size: 1.1em;
  color: #303133;
  font-weight: 500;
  flex-grow: 1;
}

.course-tag {
  margin-left: 10px;
  flex-shrink: 0;
}

.file-meta {
  font-size: 0.85em;
  color: #909399;
  display: flex;
  flex-wrap: wrap;
  gap: 10px; /* Spacing between meta items */
  margin-bottom: 8px;
}

.meta-item {
  display: flex;
  align-items: center;
}

.meta-item .el-icon {
  margin-right: 4px;
}

.file-description {
  font-size: 0.9em;
  color: #606266;
  margin-top: 5px;
  margin-bottom: 12px;
  line-height: 1.5;
  white-space: pre-wrap; /* To respect newlines in description */
}

.item-actions {
  display: flex;
  justify-content: flex-end; /* Align buttons to the right */
  gap: 10px;
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px solid #f0f0f0; /* Subtle separator */
}
</style>
