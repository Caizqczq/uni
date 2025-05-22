package com.unilife.controller;

import com.unilife.model.dto.CommentRequestDto;
import com.unilife.model.dto.CommentResponseDto;
import com.unilife.service.CommentService;
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
public class CommentController {

    private final CommentService commentService;

    // @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // @PostMapping("/comments")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object createComment(
            /*@RequestBody*/ CommentRequestDto commentRequestDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            CommentResponseDto createdComment = commentService.createComment(commentRequestDto, username);
            // return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
            return createdComment;
        } catch (Exception e) { // Catch ResourceNotFoundException, IllegalArgumentException
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping("/posts/{postId}/comments")
    public /*ResponseEntity<?>*/ Object getCommentsByPostId(/*@PathVariable*/ Long postId) {
        try {
            List<CommentResponseDto> comments = commentService.getCommentsByPostId(postId);
            // return ResponseEntity.ok(comments);
            return comments;
        } catch (Exception e) { // Catch ResourceNotFoundException if post doesn't exist
             // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }
    
    // @GetMapping("/comments/{commentId}/replies")
    public /*ResponseEntity<?>*/ Object getRepliesToComment(/*@PathVariable*/ Long commentId) {
        try {
            List<CommentResponseDto> replies = commentService.getRepliesToComment(commentId);
            // return ResponseEntity.ok(replies);
            return replies;
        } catch (Exception e) { // Catch ResourceNotFoundException if parent comment doesn't exist
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }


    // @PutMapping("/comments/{id}")
    // @PreAuthorize("isAuthenticated()") // Further check in service for ownership
    public /*ResponseEntity<?>*/ Object updateComment(
            /*@PathVariable("id")*/ Long commentId,
            /*@RequestBody*/ CommentRequestDto commentRequestDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            CommentResponseDto updatedComment = commentService.updateComment(commentId, commentRequestDto, username);
            // return ResponseEntity.ok(updatedComment);
            return updatedComment;
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @DeleteMapping("/comments/{id}")
    // @PreAuthorize("isAuthenticated()") // Further check in service for ownership or admin role
    public /*ResponseEntity<?>*/ Object deleteComment(
            /*@PathVariable("id")*/ Long commentId,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            commentService.deleteComment(commentId, username);
            // return ResponseEntity.noContent().build();
            return Map.of("message", "Comment deleted successfully");
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }
}
