package com.unilife.controller;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.model.dto.StudyGoalDto;
import com.unilife.service.StudyPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails; // Or your custom UserPrincipal
// import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // For error responses
import org.springframework.http.HttpStatus; // For ResponseEntity
import org.springframework.http.ResponseEntity; // For ResponseEntity

// @RestController
// @RequestMapping("/api/study-plans/goals")
@Tag(name = "Study Plan & Goal Management", description = "APIs for managing user study goals.")
@SecurityRequirement(name = "bearerAuth")
public class StudyPlanController {

    private final StudyPlanService studyPlanService;

    // @Autowired
    public StudyPlanController(StudyPlanService studyPlanService) {
        this.studyPlanService = studyPlanService;
    }

    @Operation(summary = "Create a new study goal", description = "Allows authenticated users to create a new study goal, optionally linking it to a course.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Study goal created successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudyGoalDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., invalid target date, course not found)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    // @PostMapping
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object createStudyGoal(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the study goal to create.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = StudyGoalDto.class)))
            /*@RequestBody*/ StudyGoalDto goalDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            StudyGoalDto createdGoal = studyPlanService.createStudyGoal(goalDto, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGoal);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get a specific study goal by ID", description = "Retrieves details of a specific study goal belonging to the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved study goal",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudyGoalDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not own this goal)"),
            @ApiResponse(responseCode = "404", description = "Study goal not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/{goalId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object getStudyGoalById(
            @Parameter(description = "ID of the study goal to retrieve.", required = true, example = "1") /*@PathVariable*/ Long goalId,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            StudyGoalDto goal = studyPlanService.getStudyGoalById(goalId, username);
            return ResponseEntity.ok(goal);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Catch generic RuntimeException for AccessDenied placeholder
            if (e.getMessage() != null && e.getMessage().contains("User not authorized")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get user's study goals", description = "Retrieves a list of study goals for the authenticated user, optionally filtered by status and/or course ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved study goals",
                         content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StudyGoalDto.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<List<StudyGoalDto>>*/ Object getUserStudyGoals(
            @Parameter(description = "Optional status to filter goals by (e.g., PENDING, IN_PROGRESS, COMPLETED).", example = "PENDING")
            /*@RequestParam(required = false)*/ String status,
            @Parameter(description = "Optional course ID to filter goals by.", example = "1")
            /*@RequestParam(required = false)*/ Long courseId,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            List<StudyGoalDto> goals = studyPlanService.getUserStudyGoals(username, status, courseId);
            return ResponseEntity.ok(goals);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Update a study goal", description = "Allows authenticated users to update an existing study goal they own.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Study goal updated successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudyGoalDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not own this goal)"),
            @ApiResponse(responseCode = "404", description = "Study goal not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @PutMapping("/{goalId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object updateStudyGoal(
            @Parameter(description = "ID of the study goal to update.", required = true, example = "1") /*@PathVariable*/ Long goalId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated details for the study goal.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = StudyGoalDto.class)))
            /*@RequestBody*/ StudyGoalDto goalDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            StudyGoalDto updatedGoal = studyPlanService.updateStudyGoal(goalId, goalDto, username);
            return ResponseEntity.ok(updatedGoal);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Catch generic RuntimeException for AccessDenied placeholder
            if (e.getMessage() != null && e.getMessage().contains("User not authorized")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Delete a study goal", description = "Allows authenticated users to delete a study goal they own.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Study goal deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not own this goal)"),
            @ApiResponse(responseCode = "404", description = "Study goal not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @DeleteMapping("/{goalId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object deleteStudyGoal(
            @Parameter(description = "ID of the study goal to delete.", required = true, example = "1") /*@PathVariable*/ Long goalId,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            studyPlanService.deleteStudyGoal(goalId, username);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Catch generic RuntimeException for AccessDenied placeholder
            if (e.getMessage() != null && e.getMessage().contains("User not authorized")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // Optional StudyTask endpoints would go here if implemented
}
