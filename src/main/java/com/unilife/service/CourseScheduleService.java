package com.unilife.service;

import com.unilife.model.dto.CourseDto;
import java.util.List;

public interface CourseScheduleService {
    CourseDto addCourse(CourseDto courseDto, String username);
    CourseDto getCourseById(Long courseId, String username);
    List<CourseDto> getUserSchedule(String username);
    List<CourseDto> getUserScheduleByDay(String username, int dayOfWeek); // Added for convenience
    CourseDto updateCourse(Long courseId, CourseDto courseDto, String username);
    void deleteCourse(Long courseId, String username);
    void importScheduleFromApi(String username, Object scheduleDataFromApi); // Placeholder
    void clearUserSchedule(String username);
}
