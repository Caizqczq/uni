package com.unilife.controller;

import com.unilife.model.dto.StudyGoalDto;
import com.unilife.service.StudyPlanService;
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
// @RequestMapping("/api/study-plans/goals")
public class StudyPlanController {

    private final StudyPlanService studyPlanService;

    // @Autowired
    public StudyPlanController(StudyPlanService studyPlanService) {
        this.studyPlanService = studyPlanService;
    }

    // @PostMapping
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object createStudyGoal(
            /*@RequestBody*/ StudyGoalDto goalDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            StudyGoalDto createdGoal = studyPlanService.createStudyGoal(goalDto, username);
            // return ResponseEntity.status(HttpStatus.CREATED).body(createdGoal);
            return createdGoal;
        } catch (Exception e) { // Catch ResourceNotFoundException, IllegalArgumentException
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping("/{goalId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object getStudyGoalById(
            /*@PathVariable*/ Long goalId,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            StudyGoalDto goal = studyPlanService.getStudyGoalById(goalId, username);
            // return ResponseEntity.ok(goal);
            return goal;
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<List<StudyGoalDto>>*/ Object getUserStudyGoals(
            /*@RequestParam(required = false)*/ String status,
            /*@RequestParam(required = false)*/ Long courseId,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            List<StudyGoalDto> goals = studyPlanService.getUserStudyGoals(username, status, courseId);
            // return ResponseEntity.ok(goals);
            return goals;
        } catch (Exception e) { // Catch ResourceNotFoundException
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @PutMapping("/{goalId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object updateStudyGoal(
            /*@PathVariable*/ Long goalId,
            /*@RequestBody*/ StudyGoalDto goalDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            StudyGoalDto updatedGoal = studyPlanService.updateStudyGoal(goalId, goalDto, username);
            // return ResponseEntity.ok(updatedGoal);
            return updatedGoal;
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException, IllegalArgumentException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @DeleteMapping("/{goalId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object deleteStudyGoal(
            /*@PathVariable*/ Long goalId,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            studyPlanService.deleteStudyGoal(goalId, username);
            // return ResponseEntity.noContent().build();
            return Map.of("message", "Study goal deleted successfully");
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // Optional StudyTask endpoints would go here if implemented
}
