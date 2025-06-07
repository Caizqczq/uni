package com.unilife.controller;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.model.dto.SharedDocumentRequestDto;
import com.unilife.model.dto.SharedDocumentResponseDto;
import com.unilife.service.SharedDocumentService;
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
// @RequestMapping("/api") // Base path, specific paths in methods
@Tag(name = "Shared Document Management", description = "APIs for managing collaborative shared documents.")
@SecurityRequirement(name = "bearerAuth")
public class SharedDocumentController {

    private final SharedDocumentService sharedDocumentService;

    // @Autowired
    public SharedDocumentController(SharedDocumentService sharedDocumentService) {
        this.sharedDocumentService = sharedDocumentService;
    }

    @Operation(summary = "Create a new shared document", description = "Allows authenticated users to create a new shared document, associating it with a course.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Shared document created successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = SharedDocumentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., course not found)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    // @PostMapping("/shared-documents")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object createSharedDocument(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the shared document to create.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = SharedDocumentRequestDto.class)))
            /*@RequestBody*/ SharedDocumentRequestDto docDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            SharedDocumentResponseDto createdDocument = sharedDocumentService.createSharedDocument(docDto, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDocument);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get a shared document by ID", description = "Retrieves a specific shared document by its ID. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved shared document",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = SharedDocumentResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Shared document not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/shared-documents/{id}")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object getSharedDocumentById(
            @Parameter(description = "ID of the shared document to retrieve.", required = true, example = "1")
            /*@PathVariable*/ Long id) {
        try {
            SharedDocumentResponseDto document = sharedDocumentService.getSharedDocumentById(id);
            return ResponseEntity.ok(document);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get shared documents for a course", description = "Retrieves a list of shared documents associated with a specific course. Results are paginated. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved shared documents for the course",
                         content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SharedDocumentResponseDto.class)))), // Assuming service returns List for now
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Course not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/courses/{courseId}/shared-documents")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object getSharedDocumentsByCourseId(
            @Parameter(description = "ID of the course to retrieve shared documents for.", required = true, example = "1")
            /*@PathVariable*/ Long courseId,
            @Parameter(description = "Page number (0-indexed).", example = "0") /*@RequestParam(defaultValue = "0")*/ int page,
            @Parameter(description = "Number of documents per page.", example = "10") /*@RequestParam(defaultValue = "10")*/ int size) {
        try {
            List<SharedDocumentResponseDto> documents = sharedDocumentService.getSharedDocumentsByCourseId(courseId, page, size);
            return ResponseEntity.ok(documents); // If service returns PageResponse, this would be it.
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Update a shared document", description = "Allows authenticated users to update a shared document. Specific permissions (e.g., ownership) are checked in the service.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shared document updated successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = SharedDocumentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., course not found if changed)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not have permission)"),
            @ApiResponse(responseCode = "404", description = "Shared document not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @PutMapping("/shared-documents/{id}")
    // @PreAuthorize("isAuthenticated()") // Further permission checks in service
    public /*ResponseEntity<?>*/ Object updateSharedDocument(
            @Parameter(description = "ID of the shared document to update.", required = true, example = "1") /*@PathVariable*/ Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated details for the shared document.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = SharedDocumentRequestDto.class)))
            /*@RequestBody*/ SharedDocumentRequestDto docDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            SharedDocumentResponseDto updatedDocument = sharedDocumentService.updateSharedDocument(id, docDto, username);
            return ResponseEntity.ok(updatedDocument);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Catch generic RuntimeException for AccessDenied placeholder
            if (e.getMessage() != null && e.getMessage().contains("User not authorized")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Delete a shared document", description = "Allows authenticated users to delete a shared document. Specific permissions (e.g., ownership or admin role) are checked in the service.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Shared document deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not have permission)"),
            @ApiResponse(responseCode = "404", description = "Shared document not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @DeleteMapping("/shared-documents/{id}")
    // @PreAuthorize("isAuthenticated()") // Further permission checks in service
    public /*ResponseEntity<?>*/ Object deleteSharedDocument(
            @Parameter(description = "ID of the shared document to delete.", required = true, example = "1") /*@PathVariable*/ Long id,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            sharedDocumentService.deleteSharedDocument(id, username);
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
}
