package com.unilife.controller;

import com.unilife.model.dto.CourseDto;
import com.unilife.service.CourseScheduleService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails; // Or your custom UserPrincipal
// import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // For error responses

// @RestController
// @RequestMapping("/api/schedule/courses")
public class CourseScheduleController {

    private final CourseScheduleService courseScheduleService;

    // @Autowired
    public CourseScheduleController(CourseScheduleService courseScheduleService) {
        this.courseScheduleService = courseScheduleService;
    }

    // @PostMapping
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object addCourse(
            /*@RequestBody*/ CourseDto courseDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            CourseDto createdCourse = courseScheduleService.addCourse(courseDto, username);
            // return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
            return createdCourse;
        } catch (Exception e) { // Catch ResourceNotFoundException, IllegalArgumentException
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping("/{courseId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object getCourseById(
            /*@PathVariable*/ Long courseId,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            CourseDto course = courseScheduleService.getCourseById(courseId, username);
            // return ResponseEntity.ok(course);
            return course;
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<List<CourseDto>>*/ Object getUserSchedule(
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            List<CourseDto> schedule = courseScheduleService.getUserSchedule(username);
            // return ResponseEntity.ok(schedule);
            return schedule;
        } catch (Exception e) { // Catch ResourceNotFoundException
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }
    
    // Example: Get schedule by day
    // @GetMapping("/day/{dayOfWeek}")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<List<CourseDto>>*/ Object getUserScheduleByDay(
            /*@PathVariable*/ int dayOfWeek,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            List<CourseDto> schedule = courseScheduleService.getUserScheduleByDay(username, dayOfWeek);
            // return ResponseEntity.ok(schedule);
            return schedule;
        } catch (Exception e) {
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }


    // @PutMapping("/{courseId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object updateCourse(
            /*@PathVariable*/ Long courseId,
            /*@RequestBody*/ CourseDto courseDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            CourseDto updatedCourse = courseScheduleService.updateCourse(courseId, courseDto, username);
            // return ResponseEntity.ok(updatedCourse);
            return updatedCourse;
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException, IllegalArgumentException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @DeleteMapping("/{courseId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object deleteCourse(
            /*@PathVariable*/ Long courseId,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            courseScheduleService.deleteCourse(courseId, username);
            // return ResponseEntity.noContent().build();
            return Map.of("message", "Course deleted successfully");
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @DeleteMapping("/all")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object clearUserSchedule(
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            courseScheduleService.clearUserSchedule(username);
            // return ResponseEntity.ok(Map.of("message", "User schedule cleared successfully"));
            return Map.of("message", "User schedule cleared successfully");
        } catch (Exception e) { // Catch ResourceNotFoundException
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @PostMapping("/import")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object importSchedule(
            /*@RequestBody*/ Object scheduleDataFromApi, // Define a DTO for this if format is known
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            courseScheduleService.importScheduleFromApi(username, scheduleDataFromApi);
            // return ResponseEntity.ok(Map.of("message", "Schedule imported successfully"));
            return Map.of("message", "Schedule imported successfully");
        } catch (Exception e) {
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Error importing schedule: " + e.getMessage()));
            return Map.of("error", "Error importing schedule: " + e.getMessage());
        }
    }
}
