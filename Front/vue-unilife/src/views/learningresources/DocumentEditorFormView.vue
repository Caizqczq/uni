<template>
  <div class="document-editor-view-container">
    <el-page-header @back="goBack" :content="pageTitle" class="page-header" />
    <div v-if="isLoadingInitialData" class="loading-section">
      <el-skeleton :rows="6" animated />
    </div>
    <div v-else-if="initialError" class="error-section">
       <el-alert :title="initialError" type="error" show-icon :closable="false" />
       <el-button @click="goBackToResources" style="margin-top:20px;">Back to Resources</el-button>
    </div>
    <DocumentEditorForm
      v-else
      :is-edit-mode="isEditMode"
      :initial-data="documentInitialData"
      :is-loading="isSubmitting"
      :fixed-course-id="fixedCourseIdForCreation"
      @submit-document="handleSubmit"
      @cancel="handleCancel"
    />
    <div v-if="resourcesStore.error && !isSubmitting && !isLoadingInitialData" class="error-message-global">
      <el-alert :title="resourcesStore.error" type="error" show-icon :closable="false" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter }_from 'vue-router';
import { useLearningResourcesStore } from '../../store/learningResources';
import { useAuthStore } from '../../store/auth';
import DocumentEditorForm from '../../components/learningresources/DocumentEditorForm.vue';
import { ElMessage, ElPageHeader, ElAlert, ElSkeleton, ElButton } from 'element-plus';

interface DocumentFormData {
  title: string;
  content: string;
  courseId: number;
}

const route = useRoute();
const router = useRouter();
const resourcesStore = useLearningResourcesStore();
const authStore = useAuthStore();

const docId = ref(route.params.docId as string | undefined);
const courseIdFromRoute = ref(route.params.courseId as string | undefined);

const isEditMode = computed(() => !!docId.value);
const pageTitle = computed(() => (isEditMode.value ? 'Edit Shared Document' : 'Create New Shared Document'));

const documentInitialData = ref<Partial<DocumentFormData> | null>(null);
const isLoadingInitialData = ref(false);
const isSubmitting = ref(false);
const initialError = ref<string | null>(null);

// For creation mode when courseId is passed in route
const fixedCourseIdForCreation = computed(() => {
    if (!isEditMode.value && courseIdFromRoute.value) {
        return parseInt(courseIdFromRoute.value, 10);
    }
    return undefined; // Let the form handle it or use initialData for edit
});


const loadDocumentForEditing = async () => {
  if (isEditMode.value && docId.value) {
    isLoadingInitialData.value = true;
    initialError.value = null;
    resourcesStore.error = null;
    try {
      await resourcesStore.fetchDocumentById(parseInt(docId.value, 10));
      if (resourcesStore.currentDocument) {
        // Basic permission check placeholder - backend should enforce strictly
        // if (authStore.user?.username !== resourcesStore.currentDocument.createdByUsername && authStore.user?.username !== resourcesStore.currentDocument.lastUpdatedByUsername) {
        //   ElMessage.error('You do not have permission to edit this document.');
        //   initialError.value = 'Permission Denied. You are not the creator or last updater of this document.';
        //   // router.push({ name: 'SharedDocuments' }); // Or to document detail
        //   return;
        // }
        documentInitialData.value = {
          title: resourcesStore.currentDocument.title,
          content: resourcesStore.currentDocument.content,
          courseId: resourcesStore.currentDocument.courseId,
        };
      } else if (resourcesStore.error) {
         initialError.value = 'Failed to load document: ' + resourcesStore.error;
      } else {
         initialError.value = 'Document not found.';
      }
    } catch (error: any) {
      initialError.value = 'Failed to load document for editing: ' + (error.message || 'Unknown error');
    } finally {
      isLoadingInitialData.value = false;
    }
  } else if (!isEditMode.value && fixedCourseIdForCreation.value) {
    // For creation with a fixed course, ensure course exists (optional, form might handle this)
    // and set initial data with this courseId
     documentInitialData.value = { courseId: fixedCourseIdForCreation.value };
     isLoadingInitialData.value = false; // No data to load, just setting fixed ID
  } else {
      isLoadingInitialData.value = false; // Not edit mode, no initial data to load from backend
  }
};

onMounted(loadDocumentForEditing);

// Watch for route changes if navigating between edit/create directly
watch(
  () => [route.params.docId, route.params.courseId],
  ([newDocId, newCourseId]) => {
    docId.value = newDocId as string | undefined;
    courseIdFromRoute.value = newCourseId as string | undefined;
    resourcesStore.clearCurrentDocument(); // Clear previous document data
    documentInitialData.value = null; // Reset initial data
    initialError.value = null;
    loadDocumentForEditing();
  }
);


const handleSubmit = async (formData: DocumentFormData) => {
  isSubmitting.value = true;
  resourcesStore.error = null;
  try {
    if (isEditMode.value && docId.value) {
      const updatedDocument = await resourcesStore.updateDocument(parseInt(docId.value, 10), formData);
      if (updatedDocument) {
        ElMessage.success('Document updated successfully!');
        router.push({ name: 'DocumentDetail', params: { docId: updatedDocument.id.toString() } });
      }
    } else {
      const newDocument = await resourcesStore.createDocument(formData);
      if (newDocument) {
        ElMessage.success('Document created successfully!');
        router.push({ name: 'DocumentDetail', params: { docId: newDocument.id.toString() } });
      }
    }
    if(resourcesStore.error && !isSubmitting.value) { // Check if error was set by store and submission ended
        ElMessage.error(resourcesStore.error);
    }
  } catch (error: any) {
    // Error should be set in store and displayed by template, or ElMessage here if not
    // ElMessage.error(resourcesStore.error || 'Operation failed: ' + (error.message || 'Unknown error'));
  } finally {
    isSubmitting.value = false;
  }
};

const handleCancel = () => {
  if (isEditMode.value && docId.value) {
    router.push({ name: 'DocumentDetail', params: { docId: docId.value } });
  } else if (fixedCourseIdForCreation.value) {
    router.push({ name: 'SharedDocumentsByCourse', params: { courseId: fixedCourseIdForCreation.value.toString() }});
  }
  else {
    router.push({ name: 'SharedDocuments' });
  }
};

const goBack = () => {
  handleCancel(); // Use the same logic for back button for now
};

const goBackToResources = () => {
    router.push({ name: 'SharedDocuments' });
}

</script>

<style scoped>
.document-editor-view-container {
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
