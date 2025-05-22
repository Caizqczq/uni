package com.unilife.controller;

import com.unilife.model.dto.CourseRelatedResourcesDto;
import com.unilife.service.CourseIntelligenceService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.web.bind.annotation.*;

import java.util.Map; // For error responses

// @RestController
// @RequestMapping("/api/courses/{courseId}/intelligent-assistance")
public class CourseIntelligenceController {

    private final CourseIntelligenceService courseIntelligenceService;

    // @Autowired
    public CourseIntelligenceController(CourseIntelligenceService courseIntelligenceService) {
        this.courseIntelligenceService = courseIntelligenceService;
    }

    // @GetMapping("/related-resources")
    // @PreAuthorize("isAuthenticated()") // Any authenticated user can access for now
    public /*ResponseEntity<?>*/ Object getRelatedResources(
            /*@PathVariable*/ Long courseId,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            CourseRelatedResourcesDto resources = courseIntelligenceService.getRelatedResourcesForCourse(courseId, username);
            // return ResponseEntity.ok(resources);
            return resources;
        } catch (Exception e) { // Catch ResourceNotFoundException if course or user not found
            // if (e instanceof ResourceNotFoundException) {
            //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // }
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred."));
            return Map.of("error", e.getMessage());
        }
    }
}
