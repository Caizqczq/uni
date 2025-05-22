package com.unilife.controller;

import com.unilife.model.dto.PostRequestDto;
import com.unilife.model.dto.PostResponseDto;
import com.unilife.service.PostService;
import com.unilife.utils.PageResponse;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails; // Or your custom UserPrincipal
// import org.springframework.web.bind.annotation.*;

import java.util.Map; // For error responses

// @RestController
// @RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    // @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // @PostMapping
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object createPost(
            /*@RequestBody*/ PostRequestDto postRequestDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) { // UserDetails or custom principal
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            PostResponseDto createdPost = postService.createPost(postRequestDto, username);
            // return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
            return createdPost;
        } catch (Exception e) {
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping("/{id}")
    public /*ResponseEntity<?>*/ Object getPostById(/*@PathVariable*/ Long id) {
        try {
            PostResponseDto post = postService.getPostById(id);
            // return ResponseEntity.ok(post);
            return post;
        } catch (Exception e) { // Catch ResourceNotFoundException
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping
    public /*ResponseEntity<PageResponse<PostResponseDto>>*/ Object getAllPosts(
            /*@RequestParam(defaultValue = "0")*/ int page,
            /*@RequestParam(defaultValue = "10")*/ int size,
            /*@RequestParam(required = false)*/ Long topicId,
            /*@RequestParam(required = false)*/ Long userId) {
        try {
            PageResponse<PostResponseDto> posts = postService.getAllPosts(page, size, topicId, userId);
            // return ResponseEntity.ok(posts);
            return posts;
        } catch (Exception e) {
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error fetching posts: " + e.getMessage()));
            return Map.of("error", "Error fetching posts: " + e.getMessage());
        }
    }

    // @PutMapping("/{id}")
    // @PreAuthorize("isAuthenticated()") // Further check in service for ownership
    public /*ResponseEntity<?>*/ Object updatePost(
            /*@PathVariable*/ Long id,
            /*@RequestBody*/ PostRequestDto postRequestDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            PostResponseDto updatedPost = postService.updatePost(id, postRequestDto, username);
            // return ResponseEntity.ok(updatedPost);
            return updatedPost;
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @DeleteMapping("/{id}")
    // @PreAuthorize("isAuthenticated()") // Further check in service for ownership or admin role
    public /*ResponseEntity<?>*/ Object deletePost(
            /*@PathVariable*/ Long id,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            // Set<String> roles = currentUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            String username = "testUser"; // Placeholder
            // boolean isAdmin = roles.contains("ROLE_ADMIN"); // Placeholder
            postService.deletePost(id, username /*, isAdmin */); // Service needs to handle ownership/admin logic
            // return ResponseEntity.noContent().build();
            return Map.of("message", "Post deleted successfully");
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @PostMapping("/{id}/like")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object likePost(
            /*@PathVariable("id")*/ Long postId,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            postService.likePost(postId, username);
            // return ResponseEntity.ok().body(Map.of("message", "Post liked successfully"));
            return Map.of("message", "Post liked successfully");
        } catch (Exception e) { // Catch ResourceNotFoundException, UserAlreadyExistsException (for already liked)
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @DeleteMapping("/{id}/like")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object unlikePost(
            /*@PathVariable("id")*/ Long postId,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            postService.unlikePost(postId, username);
            // return ResponseEntity.ok().body(Map.of("message", "Post unliked successfully"));
            return Map.of("message", "Post unliked successfully");
        } catch (Exception e) { // Catch ResourceNotFoundException
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            // For ResourceNotFoundException (if post or like not found), specific status can be returned
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping("/search")
    public /*ResponseEntity<PageResponse<PostResponseDto>>*/ Object searchPosts(
            /*@RequestParam*/ String term,
            /*@RequestParam(required = false)*/ Long topicId,
            /*@RequestParam(required = false)*/ Long authorId, // userId of the post author
            /*@RequestParam(defaultValue = "0")*/ int page,
            /*@RequestParam(defaultValue = "10")*/ int size) {
        try {
            PageResponse<PostResponseDto> posts = postService.searchPosts(term, topicId, authorId, page, size);
            // return ResponseEntity.ok(posts);
            return posts;
        } catch (Exception e) {
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error searching posts: " + e.getMessage()));
            return Map.of("error", "Error searching posts: " + e.getMessage());
        }
    }
}
