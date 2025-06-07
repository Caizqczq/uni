package com.unilife.controller;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.exception.UserAlreadyExistsException;
import com.unilife.model.dto.PostRequestDto;
import com.unilife.model.dto.PostResponseDto;
import com.unilife.service.PostService;
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
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails; // Or your custom UserPrincipal
// import org.springframework.web.bind.annotation.*;

import java.util.Map; // For error responses
import org.springframework.http.HttpStatus; // For ResponseEntity
import org.springframework.http.ResponseEntity; // For ResponseEntity

// @RestController
// @RequestMapping("/api/posts")
@Tag(name = "Forum Post Management", description = "APIs for creating, reading, updating, deleting, and interacting with forum posts.")
@SecurityRequirement(name = "bearerAuth") // Apply to all endpoints in this controller
public class PostController {

    private final PostService postService;

    // @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Create a new forum post", description = "Allows authenticated users to create a new post under a specific topic.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., topic not found)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    // @PostMapping
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object createPost(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the post to create.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = PostRequestDto.class)))
            /*@RequestBody*/ PostRequestDto postRequestDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) { // UserDetails or custom principal
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            PostResponseDto createdPost = postService.createPost(postRequestDto, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        } catch (ResourceNotFoundException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage())); // e.g. Topic not found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get a specific post by ID", description = "Retrieves details of a specific forum post by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved post",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Post not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/{id}")
    public /*ResponseEntity<?>*/ Object getPostById(
            @Parameter(description = "ID of the post to retrieve.", required = true, example = "101")
            /*@PathVariable*/ Long id) {
        try {
            PostResponseDto post = postService.getPostById(id);
            return ResponseEntity.ok(post);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get all posts (paginated)", description = "Retrieves a paginated list of all forum posts. Can be filtered by topic ID or author ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved posts",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))) // PageResponse<PostResponseDto>
    })
    // @GetMapping
    public /*ResponseEntity<PageResponse<PostResponseDto>>*/ Object getAllPosts(
            @Parameter(description = "Page number (0-indexed).", example = "0") /*@RequestParam(defaultValue = "0")*/ int page,
            @Parameter(description = "Number of posts per page.", example = "10") /*@RequestParam(defaultValue = "10")*/ int size,
            @Parameter(description = "Optional ID of the topic to filter posts by.", example = "1") /*@RequestParam(required = false)*/ Long topicId,
            @Parameter(description = "Optional ID of the user (author) to filter posts by.", example = "100") /*@RequestParam(required = false)*/ Long userId) {
        try {
            PageResponse<PostResponseDto> posts = postService.getAllPosts(page, size, topicId, userId);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error fetching posts: " + e.getMessage()));
        }
    }

    @Operation(summary = "Update an existing post", description = "Allows authenticated users to update their own posts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., topic not found if changed)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not own the post)"),
            @ApiResponse(responseCode = "404", description = "Post not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @PutMapping("/{id}")
    // @PreAuthorize("isAuthenticated()") // Further check in service for ownership
    public /*ResponseEntity<?>*/ Object updatePost(
            @Parameter(description = "ID of the post to update.", required = true, example = "101") /*@PathVariable*/ Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated details for the post.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = PostRequestDto.class)))
            /*@RequestBody*/ PostRequestDto postRequestDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            PostResponseDto updatedPost = postService.updatePost(id, postRequestDto, username);
            return ResponseEntity.ok(updatedPost);
        } catch (ResourceNotFoundException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Catch generic RuntimeException for AccessDenied placeholder
            if (e.getMessage() != null && e.getMessage().contains("User not authorized")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Delete a post", description = "Allows authenticated users to delete their own posts, or administrators to delete any post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not own the post and is not an ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Post not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @DeleteMapping("/{id}")
    // @PreAuthorize("isAuthenticated()") // Further check in service for ownership or admin role
    public /*ResponseEntity<?>*/ Object deletePost(
            @Parameter(description = "ID of the post to delete.", required = true, example = "101") /*@PathVariable*/ Long id,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            postService.deletePost(id, username);
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

    @Operation(summary = "Like a post", description = "Allows authenticated users to like a post. Liking a post multiple times by the same user is not allowed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post liked successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Bad request (e.g., post already liked)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Post or User not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @PostMapping("/{id}/like")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object likePost(
            @Parameter(description = "ID of the post to like.", required = true, example = "101") /*@PathVariable("id")*/ Long postId,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            postService.likePost(postId, username);
            return ResponseEntity.ok().body(Map.of("message", "Post liked successfully"));
        } catch (ResourceNotFoundException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (UserAlreadyExistsException e) { // For "already liked"
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Unlike a post", description = "Allows authenticated users to remove their like from a post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post unliked successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Bad request (e.g., post not liked by user)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Post or User not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @DeleteMapping("/{id}/like")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object unlikePost(
            @Parameter(description = "ID of the post to unlike.", required = true, example = "101") /*@PathVariable("id")*/ Long postId,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            postService.unlikePost(postId, username);
            return ResponseEntity.ok().body(Map.of("message", "Post unliked successfully"));
        } catch (ResourceNotFoundException e) { // Post not found or like not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Search forum posts", description = "Searches forum posts by a search term. Can be optionally filtered by topic ID and/or author ID. Results are paginated.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved search results",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))), // PageResponse<PostResponseDto>
        @ApiResponse(responseCode = "500", description = "Internal server error",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/search")
    public /*ResponseEntity<PageResponse<PostResponseDto>>*/ Object searchPosts(
            @Parameter(description = "Search term to look for in post titles and content.", required = true, example = "Tutorial") @RequestParam String term,
            @Parameter(description = "Optional ID of the topic to filter posts by.", example = "1") @RequestParam(required = false) Long topicId,
            @Parameter(description = "Optional ID of the post author to filter posts by.", example = "100") @RequestParam(required = false) Long authorId,
            @Parameter(description = "Page number (0-indexed).", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of posts per page.", example = "10") @RequestParam(defaultValue = "10") int size) {
        try {
            PageResponse<PostResponseDto> posts = postService.searchPosts(term, topicId, authorId, page, size);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error searching posts: " + e.getMessage()));
        }
    }
}
