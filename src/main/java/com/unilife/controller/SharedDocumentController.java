package com.unilife.controller;

import com.unilife.model.dto.SharedDocumentRequestDto;
import com.unilife.model.dto.SharedDocumentResponseDto;
import com.unilife.service.SharedDocumentService;
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
// @RequestMapping("/api") // Base path, specific paths in methods
public class SharedDocumentController {

    private final SharedDocumentService sharedDocumentService;

    // @Autowired
    public SharedDocumentController(SharedDocumentService sharedDocumentService) {
        this.sharedDocumentService = sharedDocumentService;
    }

    // @PostMapping("/shared-documents")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object createSharedDocument(
            /*@RequestBody*/ SharedDocumentRequestDto docDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            SharedDocumentResponseDto createdDocument = sharedDocumentService.createSharedDocument(docDto, username);
            // return ResponseEntity.status(HttpStatus.CREATED).body(createdDocument);
            return createdDocument;
        } catch (Exception e) { // Catch ResourceNotFoundException (e.g., if user or course not found)
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping("/shared-documents/{id}")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object getSharedDocumentById(/*@PathVariable*/ Long id) {
        try {
            SharedDocumentResponseDto document = sharedDocumentService.getSharedDocumentById(id);
            // return ResponseEntity.ok(document);
            return document;
        } catch (Exception e) { // Catch ResourceNotFoundException
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping("/courses/{courseId}/shared-documents")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object getSharedDocumentsByCourseId(
            /*@PathVariable*/ Long courseId,
            /*@RequestParam(defaultValue = "0")*/ int page,
            /*@RequestParam(defaultValue = "10")*/ int size) {
        try {
            // Assuming SharedDocumentService returns List for now, adjust if it returns PageResponse
            List<SharedDocumentResponseDto> documents = sharedDocumentService.getSharedDocumentsByCourseId(courseId, page, size);
            // return ResponseEntity.ok(documents);
            return documents;
        } catch (Exception e) { // Catch ResourceNotFoundException if course doesn't exist
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @PutMapping("/shared-documents/{id}")
    // @PreAuthorize("isAuthenticated()") // Further permission checks in service
    public /*ResponseEntity<?>*/ Object updateSharedDocument(
            /*@PathVariable*/ Long id,
            /*@RequestBody*/ SharedDocumentRequestDto docDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            SharedDocumentResponseDto updatedDocument = sharedDocumentService.updateSharedDocument(id, docDto, username);
            // return ResponseEntity.ok(updatedDocument);
            return updatedDocument;
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @DeleteMapping("/shared-documents/{id}")
    // @PreAuthorize("isAuthenticated()") // Further permission checks in service
    public /*ResponseEntity<?>*/ Object deleteSharedDocument(
            /*@PathVariable*/ Long id,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            sharedDocumentService.deleteSharedDocument(id, username);
            // return ResponseEntity.noContent().build();
            return Map.of("message", "Shared document deleted successfully");
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }
}
