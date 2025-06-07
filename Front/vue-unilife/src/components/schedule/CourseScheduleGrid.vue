<template>
  <div class="schedule-grid-container">
    <div class="schedule-grid">
      <!-- Time Slot Headers -->
      <div class="time-slots-header"></div> <!-- Empty cell for top-left corner -->
      <div v-for="day in daysOfWeek" :key="day.id" class="day-header">{{ day.name }}</div>

      <!-- Time Slots and Course Blocks -->
      <template v-for="timeSlot in timeSlots" :key="timeSlot.time">
        <div class="time-slot-label">{{ timeSlot.label }}</div>
        <div v-for="day in daysOfWeek" :key="`${day.id}-${timeSlot.time}`" class="grid-cell">
          <template v-for="course in getCoursesForSlot(day.id, timeSlot.time)" :key="course.id">
            <CourseBlock
              :course="course"
              :style="getCourseBlockStyle(course)"
              @edit-course="handleEditCourse"
              @delete-course="handleDeleteCourse"
            />
          </template>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits, computed } from 'vue';
import CourseBlock from './CourseBlock.vue';
import { ElMessage } from 'element-plus'; // For placeholder actions

interface CourseDto {
  id?: number;
  courseName: string;
  teacherName?: string;
  classroom?: string;
  dayOfWeek: number; // 1-7 (Monday-Sunday)
  startTime: string; // HH:mm
  endTime: string;   // HH:mm
  weekType?: number;
  startWeek?: number;
  endWeek?: number;
  notes?: string;
  color?: string;
}

const props = defineProps<{
  courses: CourseDto[];
}>();

const emit = defineEmits(['edit-course-request', 'delete-course-request']);


const daysOfWeek = [
  { id: 1, name: 'Mon' }, { id: 2, name: 'Tue' }, { id: 3, name: 'Wed' },
  { id: 4, name: 'Thu' }, { id: 5, name: 'Fri' }, { id: 6, name: 'Sat' }, { id: 7, name: 'Sun' }
];

const timeSlots = computed(() => {
  const slots = [];
  // Assuming schedule runs from 8 AM to 10 PM (22:00)
  for (let hour = 8; hour < 22; hour++) {
    slots.push({ time: `${String(hour).padStart(2, '0')}:00`, label: `${hour}:00` });
    // Optional: Add half-hour slots if needed
    // slots.push({ time: `${String(hour).padStart(2, '0')}:30`, label: `${hour}:30` });
  }
  return slots;
});

// Helper to convert HH:mm string to minutes from midnight
const timeToMinutes = (timeStr: string): number => {
  const [hours, minutes] = timeStr.split(':').map(Number);
  return hours * 60 + minutes;
};

const getCoursesForSlot = (dayId: number, timeSlotStr: string): CourseDto[] => {
  const slotStartTimeMinutes = timeToMinutes(timeSlotStr);
  // For simplicity, let's say a slot represents the start of an hour.
  // A course starting at 9:00 will appear in the 9:00 slot.
  // More complex logic could handle courses spanning multiple visible slots.
  return props.courses.filter(course => {
    const courseStartTimeMinutes = timeToMinutes(course.startTime);
    return course.dayOfWeek === dayId && courseStartTimeMinutes >= slotStartTimeMinutes && courseStartTimeMinutes < (slotStartTimeMinutes + 60);
  });
};


// This function calculates the style for positioning and height of course blocks.
// It's a simplified version. A real calendar would need more robust calculations.
const getCourseBlockStyle = (course: CourseDto) => {
  const slotHeight = 50; // Approximate height of one hour slot in px
  const pixelsPerMinute = slotHeight / 60;

  const startTimeMinutes = timeToMinutes(course.startTime);
  const endTimeMinutes = timeToMinutes(course.endTime);

  const durationMinutes = endTimeMinutes - startTimeMinutes;
  const height = durationMinutes * pixelsPerMinute;

  // Calculate top offset based on the start time relative to the first time slot (8:00 AM)
  const gridStartTimeMinutes = 8 * 60;
  const topOffset = (startTimeMinutes - gridStartTimeMinutes) * pixelsPerMinute;

  // This simplified version doesn't use grid-row-start/end dynamically based on this calculation
  // Instead, CourseBlock is rendered in the slot where it starts.
  // For true overlapping and spanning, CSS Grid with dynamic row start/end or absolute positioning is needed.
  // The current getCoursesForSlot places the block in its starting slot.
  // The height here is more for visual representation within that slot if it can be multi-line.
  return {
    // backgroundColor: course.color || '#A0CFFF', // Handled in CourseBlock now
    // If CourseBlock itself is not positioned absolutely in the grid cell,
    // these styles might not achieve perfect calendar layout.
    // This is a common challenge with simple grid approaches vs. dedicated calendar libs.
    minHeight: `${Math.max(height, 30)}px`, // Ensure a minimum height
    // For true grid spanning, you'd calculate grid-row-start and grid-row-end here
    // and apply them to the CourseBlock's wrapper if it's a direct grid item.
    // Since CourseBlock is inside a .grid-cell, this style is for the block itself.
  };
};

const handleEditCourse = (courseId?: number) => {
  if (courseId) {
    emit('edit-course-request', courseId);
  }
};

const handleDeleteCourse = (courseId?: number) => {
  if (courseId) {
    emit('delete-course-request', courseId);
  }
};

</script>

<style scoped>
.schedule-grid-container {
  overflow-x: auto; /* Allow horizontal scrolling if content overflows */
  background-color: #fff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.schedule-grid {
  display: grid;
  grid-template-columns: 60px repeat(7, 1fr); /* Time labels + 7 days */
  /* grid-template-rows: auto repeat(14, 50px); */ /* Header row + 14 one-hour slots (8am-10pm) */
  /* Auto rows for time slots will be created by the template loop */
  gap: 1px; /* Creates thin grid lines */
  background-color: #e0e0e0; /* Grid line color */
  border: 1px solid #e0e0e0;
  min-width: 800px; /* Ensure a minimum width for better layout */
}

.time-slots-header, .day-header, .time-slot-label, .grid-cell {
  background-color: #fff;
  padding: 8px;
  text-align: center;
  font-size: 0.85em;
}

.day-header {
  font-weight: bold;
  position: sticky; /* Sticky day headers */
  top: 0;
  z-index: 10;
  background-color: #f5f7fa; /* Slightly different background for headers */
}

.time-slot-label {
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  border-right: 1px solid #e0e0e0;
  font-size: 0.75em;
  color: #666;
  /* position: sticky;
  left: 0;
  z-index: 5; */ /* May not work well with grid layout, alternative is fixed column */
}

.grid-cell {
  position: relative; /* For potential absolute positioning of course blocks if needed */
  min-height: 50px; /* Default height for an empty slot */
  padding: 2px; /* Small padding for blocks */
  border-top: 1px solid #f0f0f0; /* Lighter horizontal lines within cells */
}
/* Remove border for first row of cells to avoid double border with header */
.schedule-grid > .grid-cell:nth-child(-n+7) { /* Targets first 7 grid-cells after time-slots-header and day-headers */
    /* This selector needs adjustment based on actual number of header cells */
}


/* Example: Style for the first row of actual data cells */
.schedule-grid .time-slot-label + .grid-cell, /* First cell in a time row */
.schedule-grid .grid-cell + .grid-cell { /* Subsequent cells in a time row */
  /* Any specific styles if needed */
}

/* This is where CourseBlock components will be placed */
/* CourseBlock styling is handled within CourseBlock.vue */
</style>
