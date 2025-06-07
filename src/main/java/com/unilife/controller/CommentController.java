package com.unilife.controller;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.model.dto.CommentRequestDto;
import com.unilife.model.dto.CommentResponseDto;
import com.unilife.service.CommentService;
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
@Tag(name = "Forum Comment Management", description = "APIs for managing comments on forum posts.")
@SecurityRequirement(name = "bearerAuth")
public class CommentController {

    private final CommentService commentService;

    // @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Create a new comment", description = "Allows authenticated users to add a comment to a post or reply to an existing comment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., post not found, parent comment not found or belongs to a different post)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    // @PostMapping("/comments")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object createComment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the comment to create.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = CommentRequestDto.class)))
            /*@RequestBody*/ CommentRequestDto commentRequestDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            CommentResponseDto createdComment = commentService.createComment(commentRequestDto, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage())); // Post or parent comment not found
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get comments for a post", description = "Retrieves all top-level comments for a specific post, ordered by creation time.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved comments",
                         content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Post not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/posts/{postId}/comments")
    public /*ResponseEntity<?>*/ Object getCommentsByPostId(
            @Parameter(description = "ID of the post to retrieve comments for.", required = true, example = "101")
            /*@PathVariable*/ Long postId) {
        try {
            List<CommentResponseDto> comments = commentService.getCommentsByPostId(postId);
            return ResponseEntity.ok(comments);
        } catch (ResourceNotFoundException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get replies to a comment", description = "Retrieves all replies to a specific parent comment, ordered by creation time.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved replies",
                         content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Parent comment not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/comments/{commentId}/replies")
    public /*ResponseEntity<?>*/ Object getRepliesToComment(
            @Parameter(description = "ID of the parent comment to retrieve replies for.", required = true, example = "201")
            /*@PathVariable*/ Long commentId) {
        try {
            List<CommentResponseDto> replies = commentService.getRepliesToComment(commentId);
            return ResponseEntity.ok(replies);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }


    @Operation(summary = "Update a comment", description = "Allows authenticated users to update their own comments.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not own the comment)"),
            @ApiResponse(responseCode = "404", description = "Comment not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @PutMapping("/comments/{id}")
    // @PreAuthorize("isAuthenticated()") // Further check in service for ownership
    public /*ResponseEntity<?>*/ Object updateComment(
            @Parameter(description = "ID of the comment to update.", required = true, example = "201") /*@PathVariable("id")*/ Long commentId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated content for the comment.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = CommentRequestDto.class))) // Technically only content is updatable from CommentRequestDto
            /*@RequestBody*/ CommentRequestDto commentRequestDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            CommentResponseDto updatedComment = commentService.updateComment(commentId, commentRequestDto, username);
            return ResponseEntity.ok(updatedComment);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Catch generic RuntimeException for AccessDenied placeholder
            if (e.getMessage() != null && e.getMessage().contains("User not authorized")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Delete a comment", description = "Allows authenticated users to delete their own comments, or administrators to delete any comment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not own the comment and is not an ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Comment not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @DeleteMapping("/comments/{id}")
    // @PreAuthorize("isAuthenticated()") // Further check in service for ownership or admin role
    public /*ResponseEntity<?>*/ Object deleteComment(
            @Parameter(description = "ID of the comment to delete.", required = true, example = "201") /*@PathVariable("id")*/ Long commentId,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            commentService.deleteComment(commentId, username);
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
