package com.unilife.controller;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.model.dto.NewsArticleDto;
import com.unilife.service.NewsService;
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
// @RequestMapping("/api/news")
@Tag(name = "Campus News Management", description = "APIs for managing campus news articles.")
public class NewsController {

    private final NewsService newsService;

    // @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Operation(summary = "Create a new news article", description = "Allows authorized users (e.g., ADMIN, EDITOR) to create a new news article.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "News article created successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewsArticleDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not have permission)")
    })
    @SecurityRequirement(name = "bearerAuth")
    // @PostMapping
    // @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR')") // Example roles
    public /*ResponseEntity<?>*/ Object createNewsArticle(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the news article to create.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = NewsArticleDto.class)))
            /*@RequestBody*/ NewsArticleDto newsArticleDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) { // UserDetails or custom principal
        try {
            // String username = currentUser.getUsername();
            String username = "adminUser"; // Placeholder for actual authenticated admin/editor username
            NewsArticleDto createdArticle = newsService.createNewsArticle(newsArticleDto, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdArticle);
        } catch (ResourceNotFoundException e) { // e.g. User not found
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get a specific news article by ID", description = "Retrieves details of a specific news article by its ID. Publicly accessible.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved news article",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewsArticleDto.class))),
            @ApiResponse(responseCode = "404", description = "News article not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/{id}")
    public /*ResponseEntity<?>*/ Object getNewsArticleById(
            @Parameter(description = "ID of the news article to retrieve.", required = true, example = "1")
            /*@PathVariable*/ Long id) {
        try {
            NewsArticleDto article = newsService.getNewsArticleById(id);
            return ResponseEntity.ok(article);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get all news articles (paginated)", description = "Retrieves a paginated list of all news articles. Publicly accessible.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved news articles",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))) // PageResponse<NewsArticleDto>
    })
    // @GetMapping
    public /*ResponseEntity<PageResponse<NewsArticleDto>>*/ Object getAllNewsArticles(
            @Parameter(description = "Page number (0-indexed).", example = "0") /*@RequestParam(defaultValue = "0")*/ int page,
            @Parameter(description = "Number of articles per page.", example = "10") /*@RequestParam(defaultValue = "10")*/ int size) {
        try {
            PageResponse<NewsArticleDto> articles = newsService.getAllNewsArticles(page, size);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error fetching news articles: " + e.getMessage()));
        }
    }

    @Operation(summary = "Update an existing news article", description = "Allows authorized users to update an existing news article.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "News article updated successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewsArticleDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not have permission)"),
            @ApiResponse(responseCode = "404", description = "News article not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    // @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR') or @newsSecurityService.isOwner(#id, principal.username)") // Example combined check
    public /*ResponseEntity<?>*/ Object updateNewsArticle(
            @Parameter(description = "ID of the news article to update.", required = true, example = "1") /*@PathVariable*/ Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated details for the news article.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = NewsArticleDto.class)))
            /*@RequestBody*/ NewsArticleDto newsArticleDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "adminUser"; // Placeholder
            NewsArticleDto updatedArticle = newsService.updateNewsArticle(id, newsArticleDto, username);
            return ResponseEntity.ok(updatedArticle);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Catch generic RuntimeException for AccessDenied placeholder
            if (e.getMessage() != null && e.getMessage().contains("User not authorized")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Delete a news article", description = "Allows authorized users to delete a news article.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "News article deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not have permission)"),
            @ApiResponse(responseCode = "404", description = "News article not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    // @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR') or @newsSecurityService.isOwner(#id, principal.username)") // Example combined check
    public /*ResponseEntity<?>*/ Object deleteNewsArticle(
            @Parameter(description = "ID of the news article to delete.", required = true, example = "1") /*@PathVariable*/ Long id,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "adminUser"; // Placeholder
            newsService.deleteNewsArticle(id, username);
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
