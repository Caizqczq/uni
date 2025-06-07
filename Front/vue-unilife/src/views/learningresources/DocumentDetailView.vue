<template>
  <div class="document-detail-view-container">
    <el-page-header @back="goBack" :content="pageTitle" class="page-header" />

    <div v-if="resourcesStore.isLoadingCurrentDocument" class="loading-section">
      <el-skeleton :rows="10" animated />
    </div>
    <div v-else-if="resourcesStore.error || !document" class="error-section">
      <el-alert
        title="Error Loading Document"
        type="error"
        :description="resourcesStore.error || 'The requested document could not be found.'"
        show-icon
        :closable="false"
      />
      <el-button @click="goBackToResources" style="margin-top: 20px;">Back to Resources</el-button>
    </div>
    <el-card v-else class="document-card">
      <template #header>
        <div class="document-header">
          <h1>{{ document.title }}</h1>
          <div class="document-meta-actions">
             <el-tag v-if="document.courseInfo" type="success" effect="light" size="small" class="course-tag-detail">
                <el-icon><ReadingIcon /></el-icon> {{ document.courseInfo.courseCode }} - {{ document.courseInfo.courseName }}
            </el-tag>
            <el-button
              type="primary"
              plain
              size="small"
              @click="navigateToEdit"
              :icon="EditIcon"
              v-if="canEdit"
            >
              Edit Document
            </el-button>
          </div>
        </div>
      </template>

      <el-descriptions border :column="2" class="document-info-meta">
        <el-descriptions-item label="Created By" label-class-name="meta-label">
          <el-icon><UserIcon /></el-icon> {{ document.createdByUsername || 'N/A' }}
        </el-descriptions-item>
        <el-descriptions-item label="Created At" label-class-name="meta-label">
          <el-icon><ClockIcon /></el-icon> {{ formatDateTime(document.createdAt) }}
        </el-descriptions-item>
        <el-descriptions-item label="Last Updated By" label-class-name="meta-label">
          <el-icon><UserIcon /></el-icon> {{ document.lastUpdatedByUsername || 'N/A' }}
        </el-descriptions-item>
        <el-descriptions-item label="Last Updated At" label-class-name="meta-label">
          <el-icon><ClockIcon /></el-icon> {{ formatDateTime(document.updatedAt) }}
        </el-descriptions-item>
        <el-descriptions-item label="Version" label-class-name="meta-label">
          <el-icon><CollectionTagIcon /></el-icon> {{ document.version }}
        </el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">Content</el-divider>
      <div class="document-content" v-html="renderMarkdown(document.content)"></div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useLearningResourcesStore } from '../../store/learningResources';
import { useAuthStore } from '../../store/auth';
import { ElPageHeader, ElCard, ElButton, ElIcon, ElSkeleton, ElAlert, ElDescriptions, ElDescriptionsItem, ElDivider, ElTag } from 'element-plus';
import { Edit as EditIcon, User as UserIcon, Clock as ClockIcon, CollectionTag as CollectionTagIcon, Reading as ReadingIcon } from '@element-plus/icons-vue';
import DOMPurify from 'dompurify';
import { marked } from 'marked';

const route = useRoute();
const router = useRouter();
const resourcesStore = useLearningResourcesStore();
const authStore = useAuthStore();

const docId = ref(route.params.docId as string);
const document = computed(() => resourcesStore.currentDocument);

const pageTitle = computed(() => {
  if (resourcesStore.isLoadingCurrentDocument) return "Loading Document...";
  return document.value?.title || "Document Detail";
});

const canEdit = computed(() => {
  // Placeholder for actual permission logic
  // For now, allow if user is the creator or last updater (or admin - not checked here)
  if (!authStore.isAuthenticated || !document.value || !authStore.user) {
    return false;
  }
  return authStore.user.username === document.value.createdByUsername ||
         authStore.user.username === document.value.lastUpdatedByUsername;
});

const loadDocument = async () => {
  resourcesStore.error = null; // Clear previous errors
  await resourcesStore.fetchDocumentById(parseInt(docId.value, 10));
};

onMounted(loadDocument);

watch(() => route.params.docId, (newId) => {
  if (newId && newId !== docId.value) {
    docId.value = newId as string;
    loadDocument();
  }
});

const navigateToEdit = () => {
  router.push({ name: 'EditDocument', params: { docId: docId.value } });
};

const goBack = () => {
  // Navigate to course-specific document list if course info is available, otherwise to general list
  if (document.value?.courseInfo?.id) {
    router.push({ name: 'SharedDocumentsByCourse', params: { courseId: document.value.courseInfo.id.toString() }});
  } else {
    router.push({ name: 'SharedDocuments' });
  }
};
const goBackToResources = () => {
    router.push({ name: 'SharedDocuments' });
}

const formatDateTime = (dateString: string) => {
  if (!dateString) return 'N/A';
  return new Date(dateString).toLocaleString();
};

const renderMarkdown = (content: string) => {
  if (!content) return '';
  return DOMPurify.sanitize(marked(content) as string);
};
</script>

<style scoped>
.document-detail-view-container {
  max-width: 1000px;
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
.document-card {
  border-radius: 8px;
}
.document-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.document-header h1 {
  margin: 0;
  font-size: 1.8em;
  color: #303133;
}
.document-meta-actions {
    display: flex;
    align-items: center;
    gap: 15px;
}
.course-tag-detail {
    font-size: 0.9em;
}
.document-info-meta {
  margin-top: 20px;
  margin-bottom: 20px;
}
.meta-label {
  font-weight: bold !important;
  min-width: 120px; /* Ensure labels align well */
}
.document-info-meta .el-icon {
  margin-right: 5px;
  vertical-align: middle;
}
.document-content {
  margin-top: 15px;
  line-height: 1.8;
  font-size: 1.05em;
  background-color: #fdfdfd; /* Slightly off-white for content area */
  padding: 15px;
  border-radius: 4px;
  border: 1px solid #f0f0f0;
}
.document-content :deep(p),
.document-content :deep(ul),
.document-content :deep(ol),
.document-content :deep(blockquote),
.document-content :deep(pre) {
  margin-bottom: 1em;
}
.document-content :deep(h1),
.document-content :deep(h2),
.document-content :deep(h3),
.document-content :deep(h4),
.document-content :deep(h5),
.document-content :deep(h6) {
  margin-top: 1.5em;
  margin-bottom: 0.5em;
  font-weight: 600;
  padding-bottom: 0.2em;
  border-bottom: 1px solid #eaecef;
}
.document-content :deep(code) {
  background-color: #f0f0f0;
  padding: 0.2em 0.4em;
  border-radius: 3px;
  font-family: 'Courier New', Courier, monospace;
}
.document-content :deep(pre code) {
  display: block;
  padding: 0.8em;
  overflow-x: auto;
}
.document-content :deep(blockquote) {
  border-left: 4px solid #dfe2e5;
  padding-left: 1em;
  color: #6a737d;
}
</style>
