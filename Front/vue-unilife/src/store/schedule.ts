import { defineStore } from 'pinia';
import axios from 'axios';
import { ref, Ref } from 'vue';
import { ElMessage } from 'element-plus'; // For user feedback

// Assuming DTOs are defined (simplified for now)
// These should match your backend DTOs
interface CourseDto {
  id?: number; // Optional for creation
  courseName: string;
  teacherName?: string;
  classroom?: string;
  dayOfWeek: number; // 1-7
  startTime: string; // HH:mm:ss or HH:mm
  endTime: string;   // HH:mm:ss or HH:mm
  weekType?: number;  // 0: all, 1: odd, 2: even
  startWeek?: number;
  endWeek?: number;
  notes?: string;
  color?: string;
  importedFromApi?: boolean;
  userId?: number; // Usually added by backend based on authenticated user
}

// Placeholder for PersonalEventDto for future integration
interface PersonalEventDto {
  id: number;
  title: string;
  startTime: string; // ISO string
  endTime: string;   // ISO string
  // ... other fields
}

export const useScheduleStore = defineStore('schedule', () => {
  // State
  const courses: Ref<CourseDto[]> = ref([]);
  const personalEvents: Ref<PersonalEventDto[]> = ref([]); // For later
  const isLoadingCourses: Ref<boolean> = ref(false);
  const isLoadingEvents: Ref<boolean> = ref(false); // For later
  const error: Ref<string | null> = ref(null);

  // Actions for Courses
  async function fetchUserSchedule() {
    isLoadingCourses.value = true;
    error.value = null;
    try {
      const response = await axios.get<CourseDto[]>('/api/schedule/courses');
      courses.value = response.data;
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || 'Failed to fetch schedule.';
      courses.value = [];
      ElMessage.error(error.value);
    } finally {
      isLoadingCourses.value = false;
    }
  }

  async function addCourse(courseData: CourseDto): Promise<CourseDto | null> {
    isLoadingCourses.value = true; // Or a specific isAddingCourse
    error.value = null;
    try {
      const response = await axios.post<CourseDto>('/api/schedule/courses', courseData);
      // await fetchUserSchedule(); // Refresh the whole schedule
      courses.value.push(response.data); // Optimistic update
      ElMessage.success('Course added successfully!');
      return response.data;
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || 'Failed to add course.';
      ElMessage.error(error.value);
      throw err;
    } finally {
      isLoadingCourses.value = false;
    }
  }

  async function updateCourse(courseId: number, courseData: CourseDto): Promise<CourseDto | null> {
    isLoadingCourses.value = true; // Or a specific isUpdatingCourse
    error.value = null;
    try {
      const response = await axios.put<CourseDto>(`/api/schedule/courses/${courseId}`, courseData);
      // await fetchUserSchedule(); // Refresh
      const index = courses.value.findIndex(c => c.id === courseId);
      if (index !== -1) {
        courses.value[index] = response.data;
      }
      ElMessage.success('Course updated successfully!');
      return response.data;
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || 'Failed to update course.';
      ElMessage.error(error.value);
      throw err;
    } finally {
      isLoadingCourses.value = false;
    }
  }

  async function deleteCourse(courseId: number): Promise<boolean> {
    isLoadingCourses.value = true; // Or a specific isDeletingCourse
    error.value = null;
    try {
      await axios.delete(`/api/schedule/courses/${courseId}`);
      // await fetchUserSchedule(); // Refresh
      courses.value = courses.value.filter(c => c.id !== courseId);
      ElMessage.success('Course deleted successfully!');
      return true;
    } catch (err: any) {
      error.value = err.response?.data?.error || err.message || 'Failed to delete course.';
      ElMessage.error(error.value);
      return false;
    } finally {
      isLoadingCourses.value = false;
    }
  }

  // Placeholder for Personal Event Actions (to be implemented later)
  // async function fetchPersonalEvents() { /* ... */ }
  // async function addPersonalEvent(eventData: PersonalEventDto) { /* ... */ }
  // ...etc.

  return {
    courses,
    personalEvents,
    isLoadingCourses,
    isLoadingEvents,
    error,
    fetchUserSchedule,
    addCourse,
    updateCourse,
    deleteCourse,
    // ...event actions
  };
});
