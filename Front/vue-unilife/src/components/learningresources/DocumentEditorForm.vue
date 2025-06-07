<template>
  <el-card class="document-form-card">
    <template #header>
      <div class="card-header">
        <span>{{ isEditMode ? 'Edit Document' : 'Create New Document' }}</span>
      </div>
    </template>
    <el-form ref="documentFormRef" :model="formData" :rules="formRules" label-position="top" @submit.prevent="handleSubmit">
      <el-form-item label="Title" prop="title">
        <el-input v-model="formData.title" placeholder="Enter document title" :disabled="isLoading" />
      </el-form-item>

      <el-form-item label="Course" prop="courseId">
         <el-select
          v-model="formData.courseId"
          placeholder="Select a course"
          :disabled="true"
          filterable
          class="course-select"
        >
          <el-option
            v-for="course in availableCourses"
            :key="course.id"
            :label="`${course.courseCode} - ${course.courseName}`"
            :value="course.id"
          />
        </el-select>
        <div v-if="isLoadingCoursesInternal" class="loading-text">Loading course...</div>
         <div v-if="!formData.courseId && !isEditMode && !props.fixedCourseId" class="info-text">
            Please select a course for this document.
        </div>
      </el-form-item>


      <el-form-item label="Content (Markdown supported)" prop="content">
        <el-input
          type="textarea"
          :rows="15"
          v-model="formData.content"
          placeholder="Write your document content here..."
          :disabled="isLoading"
        />
      </el-form-item>

      <el-form-item v-if="formData.content" label="Preview">
         <div class="markdown-preview" v-html="renderMarkdown(formData.content)"></div>
      </el-form-item>


      <el-form-item>
        <el-button type="primary" @click="handleSubmit" :loading="isLoading || internalFormLoading">
          {{ isEditMode ? 'Save Changes' : 'Create Document' }}
        </el-button>
        <el-button @click="handleCancel" :disabled="isLoading || internalFormLoading">Cancel</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch, defineProps, defineEmits, computed } from 'vue';
import { useLearningResourcesStore } from '../../store/learningResources';
import { ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElButton, ElCard, ElMessage } from 'element-plus';
import DOMPurify from 'dompurify';
import { marked } from 'marked';

interface DocumentFormData {
  title: string;
  content: string;
  courseId: number | null;
}

interface CourseOption {
    id: number;
    courseCode: string;
    courseName: string;
}


const props = defineProps<{
  initialData?: Partial<DocumentFormData> & { courseId?: number | null }; // Ensure courseId can be part of initialData
  isEditMode: boolean;
  isLoading: boolean; // External loading state from parent view
  fixedCourseId?: number | null; // Passed if creating document for a specific course
}>();

const emit = defineEmits(['submit-document', 'cancel']);

const resourcesStore = useLearningResourcesStore();
const documentFormRef = ref<InstanceType<typeof ElForm>>();
const internalFormLoading = ref(false); // For internal operations like fetching course for display
const isLoadingCoursesInternal = ref(false);

const formData = reactive<DocumentFormData>({
  title: '',
  content: '',
  courseId: props.fixedCourseId || null,
});

const availableCourses = ref<CourseOption[]>([]);

const formRules = reactive({
  title: [
    { required: true, message: 'Please enter a title', trigger: 'blur' },
    { min: 3, max: 200, message: 'Title length should be 3 to 200 characters', trigger: 'blur' },
  ],
  courseId: [{ required: true, message: 'Course association is required', trigger: 'change' }],
  content: [{ required: true, message: 'Please enter content for your document', trigger: 'blur' }],
});

const populateForm = (data?: Partial<DocumentFormData>) => {
  formData.title = data?.title || '';
  formData.content = data?.content || '';
  // If fixedCourseId is provided (e.g. creating for a specific course), it takes precedence.
  // Otherwise, use initialData's courseId (for editing), or null.
  formData.courseId = props.fixedCourseId !== undefined ? props.fixedCourseId : (data?.courseId || null);
};

onMounted(async () => {
  internalFormLoading.value = true;
  isLoadingCoursesInternal.value = true;

  if (resourcesStore.courses.length === 0) {
      try {
          await resourcesStore.fetchCourses();
      } catch (error) {
          ElMessage.error('Failed to load course list for selection.');
      }
  }

  // Prepare course options for select (even if disabled, for display)
  if (formData.courseId) {
      const currentCourse = resourcesStore.courses.find(c => c.id === formData.courseId);
      if (currentCourse) {
          availableCourses.value = [{id: currentCourse.id, courseCode: currentCourse.courseCode, courseName: currentCourse.courseName}];
      } else {
          // If the courseId is for a course not in the list (should ideally not happen if list is comprehensive)
          // or if fetching courses failed, this allows display of the ID at least.
          // A better approach might be to fetch the specific course info if not in list.
          if (props.initialData?.courseId) { // Check if initialData had a courseId
            // This path is less likely if fixedCourseId is used for creation and initialData for edit
            // For edit, initialData.courseId would be from an existing document
             availableCourses.value = [{id: props.initialData.courseId, courseCode: `ID: ${props.initialData.courseId}`, courseName: 'Loading...'}];
             // Optionally, try to fetch this specific course if not in the main list
          }
      }
  } else if (resourcesStore.courses.length > 0) {
      // This case is for if no courseId is pre-selected and we need to populate the dropdown for manual selection
      // However, current routing ensures courseId is always pre-selected or derived.
      // For safety or future flexibility, one might populate `availableCourses` with `resourcesStore.courses` here.
      // availableCourses.value = resourcesStore.courses; // Not needed if dropdown is always disabled
  }


  if (props.initialData) {
    populateForm(props.initialData);
  } else if (props.fixedCourseId) {
    formData.courseId = props.fixedCourseId;
  }

  isLoadingCoursesInternal.value = false;
  internalFormLoading.value = false;
});

watch(() => props.initialData, (newData) => {
  populateForm(newData);
}, { deep: true, immediate: true });

watch(() => props.fixedCourseId, (newCourseId) => {
    if (newCourseId !== undefined && newCourseId !== null) {
        formData.courseId = newCourseId;
        const course = resourcesStore.courses.find(c => c.id === newCourseId);
        if (course) {
            availableCourses.value = [{id: course.id, courseCode: course.courseCode, courseName: course.courseName}];
        }
    }
});


const handleSubmit = () => {
  documentFormRef.value?.validate((valid) => {
    if (valid && formData.courseId !== null) {
      emit('submit-document', { ...formData, courseId: formData.courseId });
    } else if (formData.courseId === null) {
        ElMessage.error('Course ID is missing. Cannot submit.');
    } else {
      ElMessage.error('Please correct the errors in the form.');
    }
  });
};

const handleCancel = () => {
    emit('cancel');
};

const renderMarkdown = (content: string) => {
  if (!content) return '';
  return DOMPurify.sanitize(marked(content) as string);
};

</script>

<style scoped>
.document-form-card {
  /* max-width: 800px; */ /* Let parent view control max-width */
  margin: 0 auto; /* Centering if parent doesn't control width */
  border-radius: 8px;
}
.card-header {
  font-size: 1.2em;
  font-weight: bold;
}
.course-select {
  width: 100%;
}
.loading-text, .info-text {
  font-size: 0.9em;
  color: #909399;
  margin-top: 5px;
}
.markdown-preview {
  margin-top: 15px;
  padding: 10px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background-color: #f9f9f9;
  font-size: 0.9em;
  max-height: 300px;
  overflow-y: auto;
}
.markdown-preview :deep(p),
.markdown-preview :deep(ul),
.markdown-preview :deep(ol),
.markdown-preview :deep(blockquote),
.markdown-preview :deep(pre) {
  margin-bottom: 0.8em;
}
.markdown-preview :deep(h1),
.markdown-preview :deep(h2),
.markdown-preview :deep(h3),
.markdown-preview :deep(h4),
.markdown-preview :deep(h5),
.markdown-preview :deep(h6) {
  margin-top: 1em;
  margin-bottom: 0.4em;
  font-weight: 600;
}
</style>
