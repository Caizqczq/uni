package com.unilife.controller;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.exception.UserAlreadyExistsException;
import com.unilife.model.dto.CourseInfoDto;
import com.unilife.service.CourseInfoService;
import com.unilife.utils.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
// import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // For error responses
import org.springframework.http.HttpStatus; // For ResponseEntity
import org.springframework.http.ResponseEntity; // For ResponseEntity

// @RestController
// @RequestMapping("/api/courses")
@Tag(name = "Course Information Management", description = "APIs for managing general course information (not user-specific schedules).")
public class CourseInfoController {

    private final CourseInfoService courseInfoService;

    // @Autowired
    public CourseInfoController(CourseInfoService courseInfoService) {
        this.courseInfoService = courseInfoService;
    }

    @Operation(summary = "Create new course information", description = "Allows administrators to add new general course information. Course codes must be unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course information created successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseInfoDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., course code already exists)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user is not an ADMIN)")
    })
    @SecurityRequirement(name = "bearerAuth")
    // @PostMapping
    // @PreAuthorize("hasRole('ADMIN')") // Example: Admin only, or other authorized role
    public /*ResponseEntity<?>*/ Object createCourseInfo(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the course to create.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = CourseInfoDto.class)))
            /*@RequestBody*/ CourseInfoDto courseInfoDto) {
        try {
            CourseInfoDto createdCourseInfo = courseInfoService.createCourseInfo(courseInfoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCourseInfo);
        } catch (UserAlreadyExistsException e) { // Course code conflict
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get all courses information", description = "Retrieves a list of all general course information entries.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all courses information",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseInfoDto[].class)))
    })
    // @GetMapping
    public /*ResponseEntity<List<CourseInfoDto>>*/ Object getAllCourseInfos() {
        List<CourseInfoDto> courseInfos = courseInfoService.getAllCourseInfos();
        return ResponseEntity.ok(courseInfos);
    }

    @Operation(summary = "Get course information by ID", description = "Retrieves details of a specific course information entry by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved course information",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseInfoDto.class))),
            @ApiResponse(responseCode = "404", description = "Course information not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/{id}")
    public /*ResponseEntity<?>*/ Object getCourseInfoById(
            @Parameter(description = "ID of the course information to retrieve.", required = true, example = "1")
            /*@PathVariable*/ Long id) {
        try {
            CourseInfoDto courseInfo = courseInfoService.getCourseInfoById(id);
            return ResponseEntity.ok(courseInfo);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get course information by course code", description = "Retrieves details of a specific course information entry by its unique course code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved course information",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseInfoDto.class))),
            @ApiResponse(responseCode = "404", description = "Course information not found for the given code",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/code/{courseCode}")
    public /*ResponseEntity<?>*/ Object getCourseInfoByCode(
            @Parameter(description = "Unique code of the course information to retrieve.", required = true, example = "CS101")
            /*@PathVariable*/ String courseCode) {
        try {
            CourseInfoDto courseInfo = courseInfoService.getCourseInfoByCode(courseCode);
            return ResponseEntity.ok(courseInfo);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Search course information", description = "Searches course information by a term in course code, name, or description. Results are paginated.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved search results",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))), // PageResponse<CourseInfoDto>
        @ApiResponse(responseCode = "500", description = "Internal server error",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/search")
    public /*ResponseEntity<PageResponse<CourseInfoDto>>*/ Object searchCourses(
            @Parameter(description = "Search term.", required = true, example = "Intro") /*@RequestParam*/ String term,
            @Parameter(description = "Page number (0-indexed).", example = "0") /*@RequestParam(defaultValue = "0")*/ int page,
            @Parameter(description = "Number of results per page.", example = "10") /*@RequestParam(defaultValue = "10")*/ int size) {
        try {
            PageResponse<CourseInfoDto> courseInfos = courseInfoService.searchCourses(term, page, size);
            return ResponseEntity.ok(courseInfos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error searching courses: " + e.getMessage()));
        }
    }

    @Operation(summary = "Update existing course information", description = "Allows administrators to update details of existing course information. Course codes must remain unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course information updated successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseInfoDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., course code conflict)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user is not an ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Course information not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    // @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public /*ResponseEntity<?>*/ Object updateCourseInfo(
            @Parameter(description = "ID of the course information to update.", required = true, example = "1") /*@PathVariable*/ Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated details for the course information.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = CourseInfoDto.class)))
            /*@RequestBody*/ CourseInfoDto courseInfoDto) {
        try {
            CourseInfoDto updatedCourse = courseInfoService.updateCourseInfo(id, courseInfoDto);
            return ResponseEntity.ok(updatedCourse);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (UserAlreadyExistsException e) { // Course code conflict
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Delete course information", description = "Allows administrators to delete course information. May fail if linked to shared documents/files (depending on service logic).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Course information deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user is not an ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Course information not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "409", description = "Conflict (e.g., course has linked documents)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    // @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public /*ResponseEntity<?>*/ Object deleteCourseInfo(
            @Parameter(description = "ID of the course information to delete.", required = true, example = "1")
            /*@PathVariable*/ Long id) {
        try {
            courseInfoService.deleteCourseInfo(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) { // For cases like "cannot delete course with linked items"
             return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }
}
