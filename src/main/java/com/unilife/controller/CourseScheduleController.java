package com.unilife.controller;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.model.dto.CourseDto;
import com.unilife.service.CourseScheduleService;
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
// @RequestMapping("/api/schedule/courses")
@Tag(name = "Course Schedule Management", description = "APIs for managing user-specific course schedules.")
@SecurityRequirement(name = "bearerAuth")
public class CourseScheduleController {

    private final CourseScheduleService courseScheduleService;

    // @Autowired
    public CourseScheduleController(CourseScheduleService courseScheduleService) {
        this.courseScheduleService = courseScheduleService;
    }

    @Operation(summary = "Add a course to the user's schedule", description = "Allows authenticated users to add a new course entry to their personal schedule.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course added successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., invalid time or week range)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    // @PostMapping
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object addCourse(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the course to add.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = CourseDto.class)))
            /*@RequestBody*/ CourseDto courseDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            CourseDto createdCourse = courseScheduleService.addCourse(courseDto, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get a specific course by ID from user's schedule", description = "Retrieves details of a specific course from the authenticated user's schedule.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved course",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not own this course entry)"),
            @ApiResponse(responseCode = "404", description = "Course not found in user's schedule",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/{courseId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object getCourseById(
            @Parameter(description = "ID of the course entry to retrieve.", required = true, example = "1") /*@PathVariable*/ Long courseId,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            CourseDto course = courseScheduleService.getCourseById(courseId, username);
            return ResponseEntity.ok(course);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Catch generic RuntimeException for AccessDenied placeholder
            if (e.getMessage() != null && e.getMessage().contains("User not authorized")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get user's full course schedule", description = "Retrieves all course entries for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved schedule",
                         content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CourseDto.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found (should not happen if authenticated)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<List<CourseDto>>*/ Object getUserSchedule(
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            List<CourseDto> schedule = courseScheduleService.getUserSchedule(username);
            return ResponseEntity.ok(schedule);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get user's course schedule for a specific day", description = "Retrieves all course entries for the authenticated user for a specific day of the week.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved day's schedule",
                         content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CourseDto.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // Example: Get schedule by day
    // @GetMapping("/day/{dayOfWeek}")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<List<CourseDto>>*/ Object getUserScheduleByDay(
            @Parameter(description = "Day of the week (1 for Monday, 7 for Sunday).", required = true, example = "1") /*@PathVariable*/ int dayOfWeek,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            List<CourseDto> schedule = courseScheduleService.getUserScheduleByDay(username, dayOfWeek);
            return ResponseEntity.ok(schedule);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }


    @Operation(summary = "Update a course in user's schedule", description = "Allows authenticated users to update an existing course entry in their schedule.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not own this course entry)"),
            @ApiResponse(responseCode = "404", description = "Course not found in user's schedule",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @PutMapping("/{courseId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object updateCourse(
            @Parameter(description = "ID of the course entry to update.", required = true, example = "1") /*@PathVariable*/ Long courseId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated details for the course.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = CourseDto.class)))
            /*@RequestBody*/ CourseDto courseDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            CourseDto updatedCourse = courseScheduleService.updateCourse(courseId, courseDto, username);
            return ResponseEntity.ok(updatedCourse);
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

    @Operation(summary = "Delete a course from user's schedule", description = "Allows authenticated users to delete a specific course entry from their schedule.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not own this course entry)"),
            @ApiResponse(responseCode = "404", description = "Course not found in user's schedule",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @DeleteMapping("/{courseId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object deleteCourse(
            @Parameter(description = "ID of the course entry to delete.", required = true, example = "1") /*@PathVariable*/ Long courseId,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            courseScheduleService.deleteCourse(courseId, username);
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

    @Operation(summary = "Clear user's entire course schedule", description = "Allows authenticated users to delete all course entries from their schedule.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User schedule cleared successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @DeleteMapping("/all")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object clearUserSchedule(
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            courseScheduleService.clearUserSchedule(username);
            return ResponseEntity.ok(Map.of("message", "User schedule cleared successfully"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Import course schedule from external API", description = "Allows authenticated users to import their course schedule from an external source (placeholder).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule imported successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Error importing schedule",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    // @PostMapping("/import")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object importSchedule(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Schedule data from the external API (specific format depends on the API).", required = true)
            /*@RequestBody*/ Object scheduleDataFromApi, // Define a DTO for this if format is known
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            courseScheduleService.importScheduleFromApi(username, scheduleDataFromApi);
            return ResponseEntity.ok(Map.of("message", "Schedule imported successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Error importing schedule: " + e.getMessage()));
        }
    }
}
