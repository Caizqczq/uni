<template>
  <el-container class="shared-files-view-container">
    <el-header class="page-header-container">
      <h1>Shared Files</h1>
      <p v-if="selectedCourse">Files for: <strong>{{ selectedCourse.courseCode }} - {{ selectedCourse.courseName }}</strong></p>
      <p v-else-if="!routeCourseId">Showing all shared files. Select a course to filter.</p>
    </el-header>

    <el-container>
      <el-aside width="300px" class="controls-aside" v-if="!isCourseSelectedFromRoute">
        <el-card shadow="never" class="filter-card">
          <template #header><strong>Filter by Course</strong></template>
          <el-select
            v-model="filterCourseId"
            placeholder="Select a course to filter"
            clearable
            filterable
            class="course-filter-select"
            @change="handleCourseFilterChange"
            :loading="resourcesStore.isLoadingCourses"
          >
            <el-option label="All Files" :value="null" />
            <el-option
              v-for="course in resourcesStore.courses"
              :key="course.id"
              :label="`${course.courseCode} - ${course.courseName}`"
              :value="course.id"
            />
          </el-select>
        </el-card>
      </el-aside>

      <el-main class="files-main">
        <el-card shadow="never" class="upload-card">
          <template #header><strong>Upload New File</strong></template>
          <el-form ref="uploadFormRef" :model="uploadForm" label-position="top">
            <el-form-item label="Select File" prop="file">
              <el-upload
                ref="uploadRef"
                action="#"
                :auto-upload="false"
                :on-change="handleFileChange"
                :on-remove="handleFileRemove"
                :limit="1"
                :file-list="fileList"
              >
                <template #trigger>
                  <el-button type="primary" :icon="UploadIcon">Select File</el-button>
                </template>
              </el-upload>
            </el-form-item>

            <el-form-item label="Description (Optional)" prop="description">
              <el-input type="textarea" :rows="2" v-model="uploadForm.description" placeholder="Enter file description" />
            </el-form-item>

            <el-form-item label="Associate with Course (Optional)" prop="courseId">
              <el-select
                v-model="uploadForm.courseId"
                placeholder="Select a course"
                clearable
                filterable
                class="course-select-upload"
                :loading="resourcesStore.isLoadingCourses"
                :disabled="!!fixedCourseIdForUpload"
              >
                <el-option
                  v-for="course in resourcesStore.courses"
                  :key="course.id"
                  :label="`${course.courseCode} - ${course.courseName}`"
                  :value="course.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-button type="success" @click="submitUpload" :loading="resourcesStore.isUploadingFile" :disabled="!uploadForm.fileRaw">
                <el-icon style="margin-right: 5px;"><UploadFilledIcon /></el-icon> Upload to Server
              </el-button>
            </el-form-item>
            <div v-if="resourcesStore.fileError" class="error-message upload-error">
                <el-alert :title="resourcesStore.fileError" type="error" show-icon :closable="false" />
            </div>
          </el-form>
        </el-card>

        <el-divider />

        <h3>Available Files</h3>
        <div v-if="resourcesStore.isLoadingFiles && resourcesStore.files.length === 0" class="loading-section">
          <el-skeleton :rows="3" animated />
        </div>
        <div v-else-if="resourcesStore.fileError && resourcesStore.files.length === 0" class="error-section">
          <el-alert title="Could not load files" type="error" :description="resourcesStore.fileError" show-icon />
        </div>
        <div v-else-if="resourcesStore.files.length === 0" class="empty-section">
          <el-empty :description="filterCourseId ? 'No files found for this course.' : 'No files shared yet.'" />
        </div>
        <div v-else class="file-list">
          <FileListItem
            v-for="file in resourcesStore.files"
            :key="file.id"
            :file="file"
            @delete-file="confirmDeleteFile"
          />
          <el-pagination
            v-if="resourcesStore.filePagination.totalPages > 1"
            background
            layout="prev, pager, next, jumper, ->, total"
            :total="resourcesStore.filePagination.totalItems"
            :page-size="resourcesStore.filePagination.pageSize"
            :current-page="resourcesStore.filePagination.currentPage + 1"
            @current-change="handleFilePageChange"
            class="pagination-controls"
          />
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useLearningResourcesStore } from '../../store/learningResources';
import { useAuthStore } from '../../store/auth';
import FileListItem from '../../components/learningresources/FileListItem.vue';
import {
  ElContainer, ElAside, ElMain, ElCard, ElSelect, ElOption, ElButton, ElIcon,
  ElSkeleton, ElAlert, ElEmpty, ElUpload, ElForm, ElFormItem, ElInput, ElPagination,
  ElMessage, ElPopconfirm, ElHeader
} from 'element-plus';
import type { UploadFile, UploadFiles, UploadInstance, UploadRawFile } from 'element-plus'
import { Upload as UploadIcon, UploadFilled as UploadFilledIcon } from '@element-plus/icons-vue';

interface CourseInfoDto {
  id: number;
  courseCode: string;
  courseName: string;
}

const route = useRoute();
const router = useRouter();
const resourcesStore = useLearningResourcesStore();
const authStore = useAuthStore();

const uploadRef = ref<UploadInstance>();
const fileList = ref<UploadFile[]>([]); // For ElUpload component state

const uploadForm = reactive({
  fileRaw: null as UploadRawFile | null,
  description: '',
  courseId: null as number | null,
});

const filterCourseId = ref<number | null>(null);
const routeCourseId = computed(() => route.params.courseId ? Number(route.params.courseId) : null);
const isCourseSelectedFromRoute = computed(() => !!routeCourseId.value);

const selectedCourse = computed(() => {
    const idToFind = routeCourseId.value || filterCourseId.value;
    if (!idToFind) return null;
    return resourcesStore.courses.find(c => c.id === idToFind) || null;
});

// Used to pre-fill and disable course selection in upload form if viewing course-specific files
const fixedCourseIdForUpload = computed(() => routeCourseId.value || filterCourseId.value);


const loadInitialCourses = async () => {
  if (resourcesStore.courses.length === 0) {
    await resourcesStore.fetchCourses();
  }
};

const loadFiles = (page?: number) => {
  const currentFilterId = routeCourseId.value || filterCourseId.value;
  const currentPage = page !== undefined ? page : resourcesStore.filePagination.currentPage;

  if (currentFilterId) {
    resourcesStore.fetchFilesByCourse(currentFilterId, currentPage, resourcesStore.filePagination.pageSize);
  } else {
    resourcesStore.fetchAllFiles(currentPage, resourcesStore.filePagination.pageSize);
  }
};

onMounted(async () => {
  resourcesStore.fileError = null; // Clear previous errors
  await loadInitialCourses(); // Load courses for filter and upload form

  if (routeCourseId.value) {
    filterCourseId.value = routeCourseId.value; // Sync filter if courseId from route
    uploadForm.courseId = routeCourseId.value; // Pre-fill upload form's courseId
  }
  loadFiles(0); // Load initial files (all or filtered by routeCourseId)
});

watch(routeCourseId, (newId) => {
    filterCourseId.value = newId; // Update filter based on route
    uploadForm.courseId = newId; // Update upload form courseId
    loadFiles(0); // Reload files for the new course
});

const handleCourseFilterChange = (newCourseId: number | null) => {
  // If user selects a course from dropdown, navigate to that course-specific view
  // or if they select "All Files", navigate to general view
  if (newCourseId) {
    router.push({ name: 'SharedFilesByCourse', params: { courseId: newCourseId.toString() } });
  } else {
    router.push({ name: 'SharedFiles' });
  }
  // The watch on routeCourseId will trigger data loading
};


const handleFileChange = (uploadFile: UploadFile, uploadFiles: UploadFiles) => {
  if (uploadFile.raw) {
    uploadForm.fileRaw = uploadFile.raw;
  }
  fileList.value = uploadFiles.slice(-1); // Keep only the last selected file
};

const handleFileRemove = () => {
  uploadForm.fileRaw = null;
};

const submitUpload = async () => {
  if (!uploadForm.fileRaw) {
    ElMessage.error('Please select a file to upload.');
    return;
  }
  if (!authStore.isAuthenticated) {
      ElMessage.error('Please login to upload files.');
      router.push('/login');
      return;
  }

  const formData = new FormData();
  formData.append('file', uploadForm.fileRaw);
  if (uploadForm.description) {
    formData.append('description', uploadForm.description);
  }
  // Use fixedCourseIdForUpload if available (from route or filter), else use uploadForm.courseId (if user selected one manually)
  const courseIdToUpload = fixedCourseIdForUpload.value || uploadForm.courseId;
  if (courseIdToUpload) {
    formData.append('courseId', courseIdToUpload.toString());
  }

  try {
    await resourcesStore.uploadFile(formData);
    ElMessage.success('File uploaded successfully!');
    uploadFormRef.value?.resetFields(); // Reset form fields
    uploadRef.value?.clearFiles(); // Clear el-upload's file list
    uploadForm.fileRaw = null;
    uploadForm.description = '';
    // Do not reset uploadForm.courseId if it was fixed by route/filter
    if(!fixedCourseIdForUpload.value) uploadForm.courseId = null;

    loadFiles(0); // Refresh the file list (go to first page)
  } catch (error) {
    // Error message is set in store and displayed by template
    // ElMessage.error(resourcesStore.fileError || 'File upload failed.');
  }
};

const confirmDeleteFile = async (fileId: number) => {
    // Confirmation is handled by ElPopconfirm in FileListItem.vue
    // This method is called after user confirms.
    if (!authStore.isAuthenticated) {
      ElMessage.error('Please login to delete files.');
      router.push('/login');
      return;
    }
    try {
        await resourcesStore.deleteFile(fileId);
        ElMessage.success('File deleted successfully.');
        // If the deleted item was on the current page and it's now empty,
        // and it wasn't the first page, navigate to the previous page.
        if (resourcesStore.files.length === 0 && resourcesStore.filePagination.currentPage > 0) {
            loadFiles(resourcesStore.filePagination.currentPage - 1);
        } else {
            loadFiles(resourcesStore.filePagination.currentPage); // Refresh current page
        }
    } catch (error) {
        // Error is set in store and displayed by template
        // ElMessage.error(resourcesStore.fileError || 'Failed to delete file.');
    }
};

const handleFilePageChange = (newPage: number) => {
  loadFiles(newPage - 1);
};

</script>

<style scoped>
.shared-files-view-container {
  padding: 20px;
  background-color: #f9fafb;
}

.page-header-container {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}
.page-header-container h1 {
  margin: 0 0 5px 0;
  font-size: 1.8em;
}
.page-header-container p {
  margin: 0;
  color: #555;
}

.controls-aside {
  padding-right: 20px;
  height: fit-content; /* Fit content or fixed height with scroll */
}

.filter-card, .upload-card {
  margin-bottom: 20px;
  border-radius: 6px;
}
.course-filter-select, .course-select-upload {
    width: 100%;
}

.files-main {
  /* Adjust as needed */
}

.upload-error {
    margin-top: 10px;
}

.loading-section, .error-section, .empty-section {
  padding: 30px;
  text-align: center;
  min-height: 150px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

.file-list {
  /* Styles for the list of files if needed */
}

.pagination-controls {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  padding: 10px 0;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
}
</style>
