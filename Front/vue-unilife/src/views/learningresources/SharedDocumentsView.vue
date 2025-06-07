<template>
  <el-container class="shared-documents-view-container">
    <el-header class="page-header-container">
      <h1>Shared Learning Resources</h1>
      <p v-if="selectedCourse">Resources for: <strong>{{ selectedCourse.courseCode }} - {{ selectedCourse.courseName }}</strong></p>
      <p v-else>Select a course to view its shared documents.</p>
    </el-header>

    <el-container>
      <el-aside width="300px" class="courses-aside" v-if="!isCourseSelectedFromRoute">
        <el-card shadow="never">
          <template #header><strong>Courses</strong></template>
          <div v-if="resourcesStore.isLoadingCourses" class="loading-section">
            <el-skeleton :rows="5" animated />
          </div>
          <div v-else-if="resourcesStore.error && resourcesStore.courses.length === 0" class="error-section">
            <el-alert title="Could not load courses" type="error" :description="resourcesStore.error" show-icon />
          </div>
          <div v-else-if="resourcesStore.courses.length === 0" class="empty-section">
            <el-empty description="No courses available." />
          </div>
          <el-menu v-else class="course-menu">
            <el-menu-item
              v-for="course in resourcesStore.courses"
              :key="course.id"
              :index="course.id.toString()"
              @click="selectCourse(course)"
              :class="{ 'is-active': selectedCourse?.id === course.id }"
            >
              <el-icon><ReadingIcon /></el-icon>
              <span>{{ course.courseCode }} - {{ course.courseName }}</span>
            </el-menu-item>
          </el-menu>
        </el-card>
      </el-aside>

      <el-main class="documents-main">
        <div v-if="selectedCourse || isCourseSelectedFromRoute">
          <el-button
            type="primary"
            @click="navigateToAddDocument"
            class="add-document-btn"
            :icon="PlusIcon"
            :disabled="!selectedCourseId"
          >
            Add Document to {{ selectedCourse?.courseCode || 'Selected Course' }}
          </el-button>

          <div v-if="resourcesStore.isLoadingDocuments" class="loading-section">
            <el-skeleton :rows="3" animated />
          </div>
          <div v-else-if="resourcesStore.error && resourcesStore.documents.length === 0" class="error-section">
            <el-alert title="Could not load documents" type="error" :description="resourcesStore.error" show-icon />
          </div>
          <div v-else-if="resourcesStore.documents.length === 0" class="empty-section">
            <el-empty :description="`No shared documents for ${selectedCourse?.courseName || 'this course'} yet.`" />
          </div>
          <div v-else class="document-list">
            <DocumentListItem
              v-for="doc in resourcesStore.documents"
              :key="doc.id"
              :document="doc"
            />
          </div>
           <!-- Pagination for documents can be added here if store supports it -->
        </div>
        <div v-else-if="!resourcesStore.isLoadingCourses && resourcesStore.courses.length > 0 && !isCourseSelectedFromRoute" class="empty-section">
           <el-empty description="Please select a course from the list to view its documents." image-size="100"></el-empty>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useLearningResourcesStore } from '../../store/learningResources';
import DocumentListItem from '../../components/learningresources/DocumentListItem.vue';
import { ElContainer, ElAside, ElMain, ElCard, ElMenu, ElMenuItem, ElButton, ElIcon, ElSkeleton, ElAlert, ElEmpty, ElPageHeader, ElMessage } from 'element-plus';
import { Reading as ReadingIcon, Plus as PlusIcon } from '@element-plus/icons-vue';
import { useAuthStore } from '../../store/auth';


interface CourseInfoDto {
  id: number;
  courseCode: string;
  courseName: string;
  description?: string;
}

const route = useRoute();
const router = useRouter();
const resourcesStore = useLearningResourcesStore();
const authStore = useAuthStore();

const selectedCourse = ref<CourseInfoDto | null>(null);
const routeCourseId = computed(() => route.params.courseId ? Number(route.params.courseId) : null);
const isCourseSelectedFromRoute = computed(() => !!routeCourseId.value);

const selectedCourseId = computed(() => selectedCourse.value?.id || routeCourseId.value);


const loadInitialData = async () => {
  resourcesStore.error = null;
  if (!isCourseSelectedFromRoute.value && resourcesStore.courses.length === 0) {
    await resourcesStore.fetchCourses();
  } else if (isCourseSelectedFromRoute.value && routeCourseId.value) {
    // If courseId is from route, fetch that course's details to display its name
    // and also its documents.
    const courseFromStore = resourcesStore.courses.find(c => c.id === routeCourseId.value);
    if (courseFromStore) {
        selectedCourse.value = courseFromStore;
    } else {
        // Try to fetch the specific course if not in list (e.g. direct navigation)
        // This might require a fetchCourseById action in the store.
        // For now, we assume fetchCourses gets all, or we rely on it being there.
        // If it's critical to show the name, ensure courses are loaded or fetch individually.
        if (resourcesStore.courses.length === 0) await resourcesStore.fetchCourses();
        const foundCourse = resourcesStore.courses.find(c => c.id === routeCourseId.value);
        if (foundCourse) selectedCourse.value = foundCourse;
        else ElMessage.warning(`Course details for ID ${routeCourseId.value} not found immediately, list might be loading.`);
    }
    await resourcesStore.fetchDocumentsByCourse(routeCourseId.value);
  }
};

onMounted(loadInitialData);

watch(routeCourseId, (newId, oldId) => {
  if (newId && newId !== oldId) {
    resourcesStore.clearDocuments(); // Clear previous course's documents
    const course = resourcesStore.courses.find(c => c.id === newId);
    selectedCourse.value = course || null;
    resourcesStore.fetchDocumentsByCourse(newId);
  } else if (!newId && isCourseSelectedFromRoute.value === false) { // Navigated away from a course-specific route to the general one
    selectedCourse.value = null;
    resourcesStore.clearDocuments();
  }
});

const selectCourse = (course: CourseInfoDto) => {
  selectedCourse.value = course;
  // Option 1: Navigate to the course-specific route
  router.push({ name: 'SharedDocumentsByCourse', params: { courseId: course.id.toString() } });
  // Option 2: Fetch documents directly (if not changing route)
  // resourcesStore.fetchDocumentsByCourse(course.id);
};

const navigateToAddDocument = () => {
  if (!authStore.isAuthenticated) {
    ElMessage.warning('Please login to add documents.');
    router.push({ name: 'Login', query: { redirect: router.currentRoute.value.fullPath }});
    return;
  }
  if (selectedCourseId.value) {
    router.push({ name: 'CreateDocument', params: { courseId: selectedCourseId.value.toString() } });
  } else {
    ElMessage.error('Please select a course first.');
  }
};

</script>

<style scoped>
.shared-documents-view-container {
  padding: 20px;
  background-color: #f9fafb; /* Lighter background for the page */
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

.courses-aside {
  padding-right: 20px;
  height: calc(100vh - 160px); /* Adjust based on header/footer */
  overflow-y: auto;
}
.courses-aside .el-card {
  height: 100%;
}

.course-menu .el-menu-item {
  display: flex;
  align-items: center;
}
.course-menu .el-menu-item .el-icon {
  margin-right: 8px;
}
.course-menu .el-menu-item.is-active {
  background-color: #ecf5ff !important; /* Element Plus active color */
  color: #409EFF;
  font-weight: bold;
}


.documents-main {
  /* background-color: #fff; */
  /* border-radius: 8px; */
  /* box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05); */
  /* padding: 20px; */
}

.add-document-btn {
  margin-bottom: 20px;
  width: 100%;
}

.loading-section, .error-section, .empty-section {
  padding: 30px;
  text-align: center;
  min-height: 200px; /* Ensure it takes some space */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #fff;
  border-radius: 4px;
   box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

.document-list {
  /* Styles for the list of documents if needed */
}
</style>
