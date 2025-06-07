package com.unilife.controller;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.model.dto.CourseRelatedResourcesDto;
import com.unilife.service.CourseIntelligenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.web.bind.annotation.*;

import java.util.Map; // For error responses
import org.springframework.http.HttpStatus; // For ResponseEntity
import org.springframework.http.ResponseEntity; // For ResponseEntity

// @RestController
// @RequestMapping("/api/courses/{courseId}/intelligent-assistance")
@Tag(name = "Course Intelligence", description = "APIs for providing intelligent assistance related to courses.")
@SecurityRequirement(name = "bearerAuth")
public class CourseIntelligenceController {

    private final CourseIntelligenceService courseIntelligenceService;

    // @Autowired
    public CourseIntelligenceController(CourseIntelligenceService courseIntelligenceService) {
        this.courseIntelligenceService = courseIntelligenceService;
    }

    @Operation(summary = "Get related learning resources for a course", description = "Retrieves a list of shared documents and files associated with a specific course. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved related resources",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseRelatedResourcesDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Course or User not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/related-resources")
    // @PreAuthorize("isAuthenticated()") // Any authenticated user can access for now
    public /*ResponseEntity<?>*/ Object getRelatedResources(
            @Parameter(description = "ID of the course to retrieve related resources for.", required = true, example = "1") /*@PathVariable*/ Long courseId,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            CourseRelatedResourcesDto resources = courseIntelligenceService.getRelatedResourcesForCourse(courseId, username);
            return ResponseEntity.ok(resources);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred."));
        }
    }
}
