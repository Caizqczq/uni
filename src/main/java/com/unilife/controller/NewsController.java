package com.unilife.controller;

import com.unilife.model.dto.NewsArticleDto;
import com.unilife.service.NewsService;
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
// @RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    // @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // @PostMapping
    // @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR')") // Example roles
    public /*ResponseEntity<?>*/ Object createNewsArticle(
            /*@RequestBody*/ NewsArticleDto newsArticleDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) { // UserDetails or custom principal
        try {
            // String username = currentUser.getUsername();
            String username = "adminUser"; // Placeholder for actual authenticated admin/editor username
            NewsArticleDto createdArticle = newsService.createNewsArticle(newsArticleDto, username);
            // return ResponseEntity.status(HttpStatus.CREATED).body(createdArticle);
            return createdArticle;
        } catch (Exception e) { // Catch more specific exceptions from service
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping("/{id}")
    public /*ResponseEntity<?>*/ Object getNewsArticleById(/*@PathVariable*/ Long id) {
        try {
            NewsArticleDto article = newsService.getNewsArticleById(id);
            // return ResponseEntity.ok(article);
            return article;
        } catch (Exception e) { // Catch ResourceNotFoundException
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping
    public /*ResponseEntity<PageResponse<NewsArticleDto>>*/ Object getAllNewsArticles(
            /*@RequestParam(defaultValue = "0")*/ int page,
            /*@RequestParam(defaultValue = "10")*/ int size) {
        try {
            PageResponse<NewsArticleDto> articles = newsService.getAllNewsArticles(page, size);
            // return ResponseEntity.ok(articles);
            return articles;
        } catch (Exception e) {
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error fetching news articles: " + e.getMessage()));
            return Map.of("error", "Error fetching news articles: " + e.getMessage());
        }
    }

    // @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR') or @newsSecurityService.isOwner(#id, principal.username)") // Example combined check
    public /*ResponseEntity<?>*/ Object updateNewsArticle(
            /*@PathVariable*/ Long id,
            /*@RequestBody*/ NewsArticleDto newsArticleDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "adminUser"; // Placeholder
            NewsArticleDto updatedArticle = newsService.updateNewsArticle(id, newsArticleDto, username);
            // return ResponseEntity.ok(updatedArticle);
            return updatedArticle;
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR') or @newsSecurityService.isOwner(#id, principal.username)") // Example combined check
    public /*ResponseEntity<?>*/ Object deleteNewsArticle(
            /*@PathVariable*/ Long id,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "adminUser"; // Placeholder
            newsService.deleteNewsArticle(id, username);
            // return ResponseEntity.noContent().build();
            return Map.of("message", "News article deleted successfully");
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }
}
