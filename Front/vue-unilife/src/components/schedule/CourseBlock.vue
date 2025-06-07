<template>
  <el-card
    class="course-block"
    :style="{ backgroundColor: course.color || '#A0CFFF', borderColor: course.color ? darkenColor(course.color, 20) : '#79BBFF' }"
    shadow="hover"
  >
    <div class="course-block-content">
      <h4 class="course-name">{{ course.courseName }}</h4>
      <p v-if="course.classroom" class="course-detail"><el-icon><LocationIcon /></el-icon> {{ course.classroom }}</p>
      <p v-if="course.teacherName" class="course-detail"><el-icon><UserIcon /></el-icon> {{ course.teacherName }}</p>
      <p class="course-detail">
        <el-icon><ClockIcon /></el-icon> {{ course.startTime }} - {{ course.endTime }}
      </p>
      <p v-if="course.startWeek && course.endWeek" class="course-detail course-weeks">
        W{{ course.startWeek }}-{{ course.endWeek }}
        <span v-if="course.weekType === 1"> (Odd)</span>
        <span v-if="course.weekType === 2"> (Even)</span>
      </p>
      <p v-if="course.notes" class="course-notes">Notes: {{ course.notes }}</p>
    </div>
    <div class="course-actions">
      <el-button size="small" plain type="primary" @click.stop="emitEdit" :icon="EditIcon">Edit</el-button>
      <el-popconfirm
          title="Delete this course?"
          confirm-button-text="Yes"
          cancel-button-text="No"
          @confirm="emitDelete"
        >
          <template #reference>
            <el-button size="small" plain type="danger" @click.stop :icon="DeleteIcon">Del</el-button>
          </template>
        </el-popconfirm>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue';
import { ElCard, ElIcon, ElButton, ElPopconfirm } from 'element-plus';
import { Location as LocationIcon, User as UserIcon, Clock as ClockIcon, Edit as EditIcon, Delete as DeleteIcon } from '@element-plus/icons-vue';

interface CourseDto {
  id?: number;
  courseName: string;
  teacherName?: string;
  classroom?: string;
  dayOfWeek: number;
  startTime: string; // HH:mm
  endTime: string;   // HH:mm
  weekType?: number;
  startWeek?: number;
  endWeek?: number;
  notes?: string;
  color?: string;
}

const props = defineProps<{
  course: CourseDto;
}>();

const emit = defineEmits(['edit-course', 'delete-course']);

const emitEdit = () => {
  emit('edit-course', props.course.id);
};

const emitDelete = () => {
  emit('delete-course', props.course.id);
};

// Helper function to darken a hex color
const darkenColor = (hex: string, percent: number): string => {
  if (!hex || !hex.startsWith('#')) return hex; // Return original if invalid
  let r = parseInt(hex.slice(1, 3), 16);
  let g = parseInt(hex.slice(3, 5), 16);
  let b = parseInt(hex.slice(5, 7), 16);

  r = Math.floor(r * (1 - percent / 100));
  g = Math.floor(g * (1 - percent / 100));
  b = Math.floor(b * (1 - percent / 100));

  r = Math.max(0, Math.min(255, r));
  g = Math.max(0, Math.min(255, g));
  b = Math.max(0, Math.min(255, b));

  return `#${r.toString(16).padStart(2, '0')}${g.toString(16).padStart(2, '0')}${b.toString(16).padStart(2, '0')}`;
};

</script>

<style scoped>
.course-block {
  padding: 10px;
  margin-bottom: 5px; /* Space between blocks in the same slot */
  border-radius: 6px;
  color: #fff; /* Default text color, assuming background will provide contrast */
  position: relative; /* For absolute positioning of actions */
  overflow: hidden; /* Ensure content respects padding and border-radius */
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
}
.course-block:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0,0,0,0.15);
}

.course-block-content {
  /* padding-bottom: 30px; */ /* Space for actions if they are at bottom */
}

.course-name {
  font-size: 0.9em;
  font-weight: bold;
  margin-top: 0;
  margin-bottom: 6px;
  word-break: break-word;
}

.course-detail {
  font-size: 0.8em;
  margin: 3px 0;
  display: flex;
  align-items: center;
}
.course-detail .el-icon {
    margin-right: 4px;
}

.course-weeks {
  font-style: italic;
}
.course-notes {
    font-size: 0.75em;
    margin-top: 5px;
    font-style: italic;
    opacity: 0.9;
    white-space: pre-wrap;
    word-break: break-word;
}

.course-actions {
  position: absolute;
  bottom: 5px;
  right: 5px;
  display: flex;
  gap: 5px;
  opacity: 0; /* Initially hidden */
  transition: opacity 0.2s ease-in-out;
}

.course-block:hover .course-actions {
  opacity: 1; /* Show on hover */
}

.course-actions .el-button {
  padding: 5px 7px;
  background-color: rgba(255, 255, 255, 0.3); /* Semi-transparent background */
  border: 1px solid rgba(255, 255, 255, 0.5);
  color: #fff;
}
.course-actions .el-button:hover {
  background-color: rgba(255, 255, 255, 0.5);
  border-color: rgba(255, 255, 255, 0.7);
}

/* Ensure text is readable on various background colors */
.course-block ::v-deep(.el-card__body) {
  padding: 10px !important; /* Override default card padding if necessary */
}

/* Adjust text color for better contrast if needed, or ensure course.color is light enough */
.course-block.dark-text { /* Add this class if course.color is very light */
  color: #303133;
}
.course-block.dark-text .course-actions .el-button {
  color: #303133;
  background-color: rgba(0, 0, 0, 0.05);
  border-color: rgba(0, 0, 0, 0.1);
}
.course-block.dark-text .course-actions .el-button:hover {
  background-color: rgba(0, 0, 0, 0.1);
  border-color: rgba(0, 0, 0, 0.2);
}
</style>
