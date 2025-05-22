package com.unilife.controller;

import com.unilife.model.dto.CourseInfoDto;
import com.unilife.service.CourseInfoService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // For error responses

// @RestController
// @RequestMapping("/api/courses")
public class CourseInfoController {

    private final CourseInfoService courseInfoService;

    // @Autowired
    public CourseInfoController(CourseInfoService courseInfoService) {
        this.courseInfoService = courseInfoService;
    }

    // @PostMapping
    // @PreAuthorize("hasRole('ADMIN')") // Example: Admin only, or other authorized role
    public /*ResponseEntity<?>*/ Object createCourseInfo(/*@RequestBody*/ CourseInfoDto courseInfoDto) {
        try {
            CourseInfoDto createdCourseInfo = courseInfoService.createCourseInfo(courseInfoDto);
            // return ResponseEntity.status(HttpStatus.CREATED).body(createdCourseInfo);
            return createdCourseInfo; // Simplified return for non-Spring Boot env
        } catch (Exception e) { // Catch more specific exceptions from service (e.g., UserAlreadyExistsException for course code conflict)
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
             return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping
    public /*ResponseEntity<List<CourseInfoDto>>*/ Object getAllCourseInfos() {
        List<CourseInfoDto> courseInfos = courseInfoService.getAllCourseInfos();
        // return ResponseEntity.ok(courseInfos);
        return courseInfos;
    }

    // @GetMapping("/{id}")
    public /*ResponseEntity<?>*/ Object getCourseInfoById(/*@PathVariable*/ Long id) {
        try {
            CourseInfoDto courseInfo = courseInfoService.getCourseInfoById(id);
            // return ResponseEntity.ok(courseInfo);
            return courseInfo;
        } catch (Exception e) { // Catch ResourceNotFoundException
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping("/code/{courseCode}")
    public /*ResponseEntity<?>*/ Object getCourseInfoByCode(/*@PathVariable*/ String courseCode) {
        try {
            CourseInfoDto courseInfo = courseInfoService.getCourseInfoByCode(courseCode);
            // return ResponseEntity.ok(courseInfo);
            return courseInfo;
        } catch (Exception e) { // Catch ResourceNotFoundException
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping("/search")
    public /*ResponseEntity<PageResponse<CourseInfoDto>>*/ Object searchCourses(
            /*@RequestParam*/ String term,
            /*@RequestParam(defaultValue = "0")*/ int page,
            /*@RequestParam(defaultValue = "10")*/ int size) {
        try {
            PageResponse<CourseInfoDto> courseInfos = courseInfoService.searchCourses(term, page, size);
            // return ResponseEntity.ok(courseInfos);
            return courseInfos;
        } catch (Exception e) {
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error searching courses: " + e.getMessage()));
            return Map.of("error", "Error searching courses: " + e.getMessage());
        }
    }
    
    // @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public /*ResponseEntity<?>*/ Object updateCourseInfo(/*@PathVariable*/ Long id, /*@RequestBody*/ CourseInfoDto courseInfoDto) {
        try {
            CourseInfoDto updatedCourse = courseInfoService.updateCourseInfo(id, courseInfoDto);
            // return ResponseEntity.ok(updatedCourse);
            return updatedCourse;
        } catch (Exception e) { // Catch ResourceNotFoundException, UserAlreadyExistsException (for code conflict)
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public /*ResponseEntity<?>*/ Object deleteCourseInfo(/*@PathVariable*/ Long id) {
        try {
            courseInfoService.deleteCourseInfo(id);
            // return ResponseEntity.noContent().build();
            return Map.of("message", "CourseInfo deleted successfully");
        } catch (Exception e) { // Catch ResourceNotFoundException, IllegalStateException (if linked docs exist)
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof IllegalStateException) return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }
}
