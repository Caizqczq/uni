package com.unilife.controller;

import com.unilife.model.dto.LearningMaterialSearchResultDto;
import com.unilife.service.LearningMaterialSearchService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.web.bind.annotation.*;

import java.util.Map; // For error responses

// @RestController
// @RequestMapping("/api/search/materials")
public class LearningMaterialSearchController {

    private final LearningMaterialSearchService learningMaterialSearchService;

    // @Autowired
    public LearningMaterialSearchController(LearningMaterialSearchService learningMaterialSearchService) {
        this.learningMaterialSearchService = learningMaterialSearchService;
    }

    // @GetMapping
    // @PreAuthorize("isAuthenticated()") // Or public, depending on requirements
    public /*ResponseEntity<?>*/ Object searchLearningMaterials(
            /*@RequestParam*/ String term,
            /*@RequestParam(required = false)*/ Long courseId,
            /*@RequestParam(defaultValue = "0")*/ int docPage,
            /*@RequestParam(defaultValue = "5")*/ int docSize,
            /*@RequestParam(defaultValue = "0")*/ int filePage,
            /*@RequestParam(defaultValue = "5")*/ int fileSize,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername(); // If needed for user-specific access control in service
            String username = "testUser"; // Placeholder for actual authenticated username
            LearningMaterialSearchResultDto results = learningMaterialSearchService.searchMaterials(
                    term, courseId, docPage, docSize, filePage, fileSize, username
            );
            // return ResponseEntity.ok(results);
            return results;
        } catch (Exception e) { // Catch ResourceNotFoundException (e.g., if user not found by service)
            // if (e instanceof ResourceNotFoundException) {
            //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // }
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred during search."));
            return Map.of("error", "An unexpected error occurred during search: " + e.getMessage());
        }
    }
}
