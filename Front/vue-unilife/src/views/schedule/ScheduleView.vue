<template>
  <el-container class="schedule-view-container">
    <el-header class="page-header-container">
      <h1>My Schedule</h1>
      <el-button type="primary" @click="openAddCourseDialog" :icon="PlusIcon">Add Course</el-button>
    </el-header>

    <el-main>
      <div v-if="scheduleStore.isLoadingCourses && scheduleStore.courses.length === 0" class="loading-section">
        <el-skeleton :rows="8" animated />
      </div>
      <div v-else-if="scheduleStore.error && scheduleStore.courses.length === 0" class="error-section">
        <el-alert title="Could not load schedule" type="error" :description="scheduleStore.error" show-icon />
      </div>
      <div v-else-if="scheduleStore.courses.length === 0 && !scheduleStore.isLoadingCourses" class="empty-section">
        <el-empty description="Your schedule is currently empty. Add some courses!" />
        <el-button type="success" @click="openAddCourseDialog" :icon="PlusIcon" style="margin-top: 20px;">
          Add Your First Course
        </el-button>
      </div>
      <CourseScheduleGrid
        v-else
        :courses="scheduleStore.courses"
        @edit-course-request="openEditCourseDialog"
        @delete-course-request="handleDeleteCourse"
      />
    </el-main>

    <!-- Add/Edit Course Dialog -->
    <el-dialog
      v-model="courseDialogVisible"
      :title="isEditMode ? 'Edit Course' : 'Add New Course'"
      width="600px"
      :before-close="handleDialogClose"
      destroy-on-close
      top="5vh"
    >
      <el-form ref="courseFormRef" :model="courseFormData" :rules="courseFormRules" label-position="top">
        <el-form-item label="Course Name" prop="courseName">
          <el-input v-model="courseFormData.courseName" placeholder="e.g., Introduction to AI" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Teacher Name (Optional)" prop="teacherName">
              <el-input v-model="courseFormData.teacherName" placeholder="e.g., Prof. Turing" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Classroom (Optional)" prop="classroom">
              <el-input v-model="courseFormData.classroom" placeholder="e.g., Room 101" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="Day of Week" prop="dayOfWeek">
          <el-select v-model="courseFormData.dayOfWeek" placeholder="Select day">
            <el-option v-for="day in daysOfWeekOptions" :key="day.value" :label="day.label" :value="day.value" />
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Start Time" prop="startTime">
              <el-time-picker v-model="courseFormData.startTime" format="HH:mm" value-format="HH:mm" placeholder="Start time" style="width: 100%;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="End Time" prop="endTime">
              <el-time-picker v-model="courseFormData.endTime" format="HH:mm" value-format="HH:mm" placeholder="End time" style="width: 100%;"/>
            </el-form-item>
          </el-col>
        </el-row>
         <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Start Week" prop="startWeek">
              <el-input-number v-model="courseFormData.startWeek" :min="1" :max="20" controls-position="right" style="width: 100%;"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="End Week" prop="endWeek">
              <el-input-number v-model="courseFormData.endWeek" :min="courseFormData.startWeek || 1" :max="20" controls-position="right" style="width: 100%;"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Week Type" prop="weekType">
              <el-select v-model="courseFormData.weekType" placeholder="Select week type" style="width: 100%;">
                <el-option label="All Weeks" :value="0" />
                <el-option label="Odd Weeks" :value="1" />
                <el-option label="Even Weeks" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="Color (Optional)" prop="color">
            <el-color-picker v-model="courseFormData.color" show-alpha :predefine="predefinedColors" />
        </el-form-item>
        <el-form-item label="Notes (Optional)" prop="notes">
          <el-input type="textarea" :rows="2" v-model="courseFormData.notes" placeholder="Any additional notes" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="courseDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitCourseForm" :loading="isSubmittingCourse">
          {{ isEditMode ? 'Save Changes' : 'Add Course' }}
        </el-button>
      </template>
    </el-dialog>

  </el-container>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue';
import { useScheduleStore } from '../store/schedule';
import CourseScheduleGrid from '../components/schedule/CourseScheduleGrid.vue';
import { ElContainer, ElHeader, ElMain, ElButton, ElIcon, ElSkeleton, ElAlert, ElEmpty, ElDialog, ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElTimePicker, ElInputNumber, ElColorPicker, ElRow, ElCol, ElMessage, ElPopconfirm } from 'element-plus';
import { Plus as PlusIcon } from '@element-plus/icons-vue';
import type { FormInstance, FormRules } from 'element-plus';

interface CourseDto {
  id?: number;
  courseName: string;
  teacherName?: string;
  classroom?: string;
  dayOfWeek: number | null;
  startTime: string; // HH:mm
  endTime: string;   // HH:mm
  weekType?: number;
  startWeek?: number;
  endWeek?: number;
  notes?: string;
  color?: string;
}

const scheduleStore = useScheduleStore();
const courseDialogVisible = ref(false);
const isEditMode = ref(false);
const courseFormRef = ref<FormInstance>();
const isSubmittingCourse = ref(false);

const initialCourseFormData: CourseDto = {
  courseName: '',
  teacherName: '',
  classroom: '',
  dayOfWeek: null,
  startTime: '',
  endTime: '',
  weekType: 0,
  startWeek: 1,
  endWeek: 16,
  notes: '',
  color: '#A0CFFF', // Default color
};
const courseFormData = reactive<CourseDto>({ ...initialCourseFormData });

const daysOfWeekOptions = [
  { value: 1, label: 'Monday' }, { value: 2, label: 'Tuesday' }, { value: 3, label: 'Wednesday' },
  { value: 4, label: 'Thursday' }, { value: 5, label: 'Friday' }, { value: 6, label: 'Saturday' }, { value: 7, label: 'Sunday' }
];

const predefinedColors = ref([
  '#ff4500', '#ff8c00', '#ffd700', '#90ee90', '#00ced1', '#1e90ff', '#c71585',
]);


const courseFormRules = reactive<FormRules>({
  courseName: [{ required: true, message: 'Please input course name', trigger: 'blur' }],
  dayOfWeek: [{ required: true, message: 'Please select day of week', trigger: 'change' }],
  startTime: [{ required: true, message: 'Please select start time', trigger: 'change' }],
  endTime: [{ required: true, message: 'Please select end time', trigger: 'change' }],
  startWeek: [{ required: true, message: 'Please input start week', trigger: 'blur' }],
  endWeek: [{ required: true, message: 'Please input end week', trigger: 'blur' }],
});


onMounted(() => {
  scheduleStore.fetchUserSchedule();
});

const resetForm = () => {
  Object.assign(courseFormData, initialCourseFormData);
  if (courseFormRef.value) {
    courseFormRef.value.clearValidate();
    courseFormRef.value.resetFields(); // This might clear to undefined, so re-assign initial
    Object.assign(courseFormData, initialCourseFormData); // ensure defaults
  }
   scheduleStore.error = null;
};

const openAddCourseDialog = () => {
  isEditMode.value = false;
  resetForm(); // Reset to default for new course
  courseDialogVisible.value = true;
};

const openEditCourseDialog = (courseId: number) => {
  const courseToEdit = scheduleStore.courses.find(c => c.id === courseId);
  if (courseToEdit) {
    isEditMode.value = true;
    // Ensure all fields are correctly populated, handle potential undefined values
    Object.assign(courseFormData, {
        ...initialCourseFormData, // Start with defaults to ensure all fields are present
        ...courseToEdit,
        dayOfWeek: courseToEdit.dayOfWeek ?? null, // Ensure null if backend might send undefined
        startTime: courseToEdit.startTime || '', // Ensure empty string if null/undefined
        endTime: courseToEdit.endTime || '',
        weekType: courseToEdit.weekType ?? 0,
        startWeek: courseToEdit.startWeek ?? 1,
        endWeek: courseToEdit.endWeek ?? 16,
        color: courseToEdit.color || initialCourseFormData.color,
    });
    courseDialogVisible.value = true;
  } else {
    ElMessage.error('Could not find course to edit.');
  }
};

const handleDialogClose = (done: () => void) => {
  resetForm();
  done();
};

const submitCourseForm = async () => {
  if (!courseFormRef.value) return;
  await courseFormRef.value.validate(async (valid) => {
    if (valid) {
      isSubmittingCourse.value = true;
      try {
        if (isEditMode.value && courseFormData.id) {
          await scheduleStore.updateCourse(courseFormData.id, { ...courseFormData });
        } else {
          await scheduleStore.addCourse({ ...courseFormData });
        }
        courseDialogVisible.value = false;
        resetForm();
        // Store actions already show success/error messages
      } catch (error) {
        // Error already handled by store and displayed via ElMessage there
        // console.error("Failed to submit course form:", error);
      } finally {
        isSubmittingCourse.value = false;
      }
    } else {
      ElMessage.error('Please correct the errors in the form.');
      return false;
    }
  });
};

const handleDeleteCourse = async (courseId?: number) => {
    if (courseId === undefined) return;
    try {
        await scheduleStore.deleteCourse(courseId);
        // Success message is in store action
    } catch (error) {
        // Error message is in store action
    }
};

</script>

<style scoped>
.schedule-view-container {
  padding: 20px;
  background-color: #f9fafb;
}

.page-header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 0 20px 0; /* No top/bottom padding, only bottom for separation */
  margin-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
  height: auto;
}

.page-header-container h1 {
  margin: 0;
  font-size: 1.8em;
  color: #303133;
}

.loading-section, .error-section, .empty-section {
  padding: 40px;
  text-align: center;
  min-height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.03);
}

.el-dialog__body {
  padding-bottom: 0px !important; /* Reduce bottom padding if footer is present */
}
</style>
