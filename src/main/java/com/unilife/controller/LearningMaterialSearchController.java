package com.unilife.controller;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.model.dto.LearningMaterialSearchResultDto;
import com.unilife.service.LearningMaterialSearchService;
import com.unilife.utils.PageResponse; // For schema reference
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
// @RequestMapping("/api/search/materials")
@Tag(name = "Learning Material Search", description = "APIs for searching shared documents and files.")
@SecurityRequirement(name = "bearerAuth")
public class LearningMaterialSearchController {

    private final LearningMaterialSearchService learningMaterialSearchService;

    // @Autowired
    public LearningMaterialSearchController(LearningMaterialSearchService learningMaterialSearchService) {
        this.learningMaterialSearchService = learningMaterialSearchService;
    }

    @Operation(summary = "Search learning materials", description = "Searches shared documents and files by a search term. Can be optionally filtered by course ID. Results are paginated separately for documents and files.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved search results",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = LearningMaterialSearchResultDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found (if user validation is strict in service)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping
    // @PreAuthorize("isAuthenticated()") // Or public, depending on requirements
    public /*ResponseEntity<?>*/ Object searchLearningMaterials(
            @Parameter(description = "Search term to look for in material titles, content, descriptions, or file names.", required = true, example = "Java Basics") /*@RequestParam*/ String term,
            @Parameter(description = "Optional ID of the course to filter materials by.", example = "1") /*@RequestParam(required = false)*/ Long courseId,
            @Parameter(description = "Page number for document results (0-indexed).", example = "0") /*@RequestParam(defaultValue = "0")*/ int docPage,
            @Parameter(description = "Number of documents per page.", example = "5") /*@RequestParam(defaultValue = "5")*/ int docSize,
            @Parameter(description = "Page number for file results (0-indexed).", example = "0") /*@RequestParam(defaultValue = "0")*/ int filePage,
            @Parameter(description = "Number of files per page.", example = "5") /*@RequestParam(defaultValue = "5")*/ int fileSize,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername(); // If needed for user-specific access control in service
            String username = "testUser"; // Placeholder for actual authenticated username
            LearningMaterialSearchResultDto results = learningMaterialSearchService.searchMaterials(
                    term, courseId, docPage, docSize, filePage, fileSize, username
            );
            return ResponseEntity.ok(results);
        } catch (ResourceNotFoundException e) { // e.g., User not found by service
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred during search."));
        }
    }
}
