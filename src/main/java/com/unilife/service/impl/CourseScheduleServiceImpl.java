package com.unilife.service.impl;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.mapper.CourseMapper;
import com.unilife.mapper.UserMapper;
import com.unilife.model.dto.CourseDto;
import com.unilife.model.entity.Course;
import com.unilife.model.entity.User;
import com.unilife.service.CourseScheduleService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// @Service
public class CourseScheduleServiceImpl implements CourseScheduleService {

    private final CourseMapper courseMapper;
    private final UserMapper userMapper;

    // @Autowired
    public CourseScheduleServiceImpl(CourseMapper courseMapper, UserMapper userMapper) {
        this.courseMapper = courseMapper;
        this.userMapper = userMapper;
    }

    @Override
    // @Transactional
    public CourseDto addCourse(CourseDto courseDto, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }

        // Basic validation (time conflict check could be added here or in controller)
        if (courseDto.getStartTime() == null || courseDto.getEndTime() == null ||
            courseDto.getStartTime().isAfter(courseDto.getEndTime())) {
            throw new IllegalArgumentException("Invalid course times. Start time must be before end time.");
        }
        if (courseDto.getStartWeek() > courseDto.getEndWeek()) {
            throw new IllegalArgumentException("Invalid week range. Start week must be before or equal to end week.");
        }

        Course course = mapToEntity(courseDto);
        course.setUserId(user.getId()); // Set the user ID

        courseMapper.save(course);
        return mapToDto(course);
    }

    @Override
    public CourseDto getCourseById(Long courseId, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        Course course = courseMapper.findById(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }
        if (!Objects.equals(course.getUserId(), user.getId())) {
            // throw new AccessDeniedException("User not authorized to access this course");
            throw new RuntimeException("User not authorized to access this course (placeholder for AccessDeniedException)");
        }
        return mapToDto(course);
    }

    @Override
    public List<CourseDto> getUserSchedule(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        List<Course> courses = courseMapper.findByUserId(user.getId());
        return courses.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> getUserScheduleByDay(String username, int dayOfWeek) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        List<Course> courses = courseMapper.findByUserIdAndDay(user.getId(), dayOfWeek);
        return courses.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    // @Transactional
    public CourseDto updateCourse(Long courseId, CourseDto courseDto, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        Course existingCourse = courseMapper.findById(courseId);
        if (existingCourse == null) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }
        if (!Objects.equals(existingCourse.getUserId(), user.getId())) {
            // throw new AccessDeniedException("User not authorized to update this course");
            throw new RuntimeException("User not authorized to update this course (placeholder for AccessDeniedException)");
        }

        // Update fields from DTO
        updateEntityFromDto(existingCourse, courseDto);
        existingCourse.setUserId(user.getId()); // Ensure userId is not changed from DTO if present

        courseMapper.update(existingCourse);
        return mapToDto(existingCourse);
    }

    @Override
    // @Transactional
    public void deleteCourse(Long courseId, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        Course course = courseMapper.findById(courseId);
        if (course == null) {
            // Optional: throw ResourceNotFoundException, or allow silent fail if already deleted
            return; // Or throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }
        if (!Objects.equals(course.getUserId(), user.getId())) {
            // throw new AccessDeniedException("User not authorized to delete this course");
            throw new RuntimeException("User not authorized to delete this course (placeholder for AccessDeniedException)");
        }
        courseMapper.deleteByIdAndUserId(courseId, user.getId());
    }

    @Override
    // @Transactional
    public void importScheduleFromApi(String username, Object scheduleDataFromApi) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }
        // Placeholder: Logic to parse scheduleDataFromApi and save courses
        // 1. Clear existing API-imported courses for this user (optional, depends on strategy)
        //    List<Course> existingApiCourses = courseMapper.findByUserId(user.getId())
        //                                          .stream().filter(Course::isImportedFromApi).toList();
        //    existingApiCourses.forEach(c -> courseMapper.deleteByIdAndUserId(c.getId(), user.getId()));
        // 2. Parse scheduleDataFromApi into a list of Course objects
        //    List<Course> importedCourses = parseApiData(scheduleDataFromApi); // Implement this
        // 3. Save each course
        //    for (Course course : importedCourses) {
        //        course.setUserId(user.getId());
        //        course.setImportedFromApi(true);
        //        courseMapper.save(course);
        //    }
        System.out.println("Placeholder: Importing schedule for user " + username + " from API data.");
        // For now, let's add a dummy imported course
        Course dummyCourse = new Course();
        dummyCourse.setUserId(user.getId());
        dummyCourse.setCourseName("Imported Intro to AI");
        dummyCourse.setTeacherName("API Professor");
        dummyCourse.setClassroom("Online");
        dummyCourse.setDayOfWeek(1); // Monday
        dummyCourse.setStartTime(java.time.LocalTime.of(10, 0));
        dummyCourse.setEndTime(java.time.LocalTime.of(11, 30));
        dummyCourse.setStartWeek(1);
        dummyCourse.setEndWeek(16);
        dummyCourse.setWeekType(0); // All weeks
        dummyCourse.setImportedFromApi(true);
        dummyCourse.setColor("#007bff");
        courseMapper.save(dummyCourse);
    }

    @Override
    // @Transactional
    public void clearUserSchedule(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        courseMapper.deleteByUserId(user.getId());
    }

    // Helper methods for mapping
    private CourseDto mapToDto(Course course) {
        if (course == null) return null;
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setUserId(course.getUserId());
        dto.setCourseName(course.getCourseName());
        dto.setTeacherName(course.getTeacherName());
        dto.setClassroom(course.getClassroom());
        dto.setDayOfWeek(course.getDayOfWeek());
        dto.setStartTime(course.getStartTime());
        dto.setEndTime(course.getEndTime());
        dto.setWeekType(course.getWeekType());
        dto.setStartWeek(course.getStartWeek());
        dto.setEndWeek(course.getEndWeek());
        dto.setNotes(course.getNotes());
        dto.setColor(course.getColor());
        dto.setImportedFromApi(course.isImportedFromApi());
        return dto;
    }

    private Course mapToEntity(CourseDto dto) {
        if (dto == null) return null;
        Course course = new Course();
        // ID is not set from DTO for creation, but could be for update if DTO carries it
        // course.setId(dto.getId()); // Not for creation
        course.setCourseName(dto.getCourseName());
        course.setTeacherName(dto.getTeacherName());
        course.setClassroom(dto.getClassroom());
        course.setDayOfWeek(dto.getDayOfWeek());
        course.setStartTime(dto.getStartTime());
        course.setEndTime(dto.getEndTime());
        course.setWeekType(dto.getWeekType());
        course.setStartWeek(dto.getStartWeek());
        course.setEndWeek(dto.getEndWeek());
        course.setNotes(dto.getNotes());
        course.setColor(dto.getColor());
        course.setImportedFromApi(dto.isImportedFromApi());
        // userId is set in the service method
        return course;
    }

    private void updateEntityFromDto(Course course, CourseDto dto) {
        if (dto.getCourseName() != null) course.setCourseName(dto.getCourseName());
        if (dto.getTeacherName() != null) course.setTeacherName(dto.getTeacherName());
        if (dto.getClassroom() != null) course.setClassroom(dto.getClassroom());
        // DayOfWeek, StartTime, EndTime usually define a course slot, so check if they are non-null if allowing changes
        course.setDayOfWeek(dto.getDayOfWeek()); // Assuming int, if not null
        course.setStartTime(dto.getStartTime()); // Assuming LocalTime
        course.setEndTime(dto.getEndTime());   // Assuming LocalTime
        course.setWeekType(dto.getWeekType());
        course.setStartWeek(dto.getStartWeek());
        course.setEndWeek(dto.getEndWeek());
        if (dto.getNotes() != null) course.setNotes(dto.getNotes());
        if (dto.getColor() != null) course.setColor(dto.getColor());
        course.setImportedFromApi(dto.isImportedFromApi());
    }
}
