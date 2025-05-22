package com.unilife.controller;

import com.unilife.model.dto.PointTransactionDto;
import com.unilife.model.dto.UserPointsDto;
import com.unilife.service.PointsService;
import com.unilife.utils.PageResponse;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // For error responses

// @RestController
// @RequestMapping("/api/points")
public class PointsController {

    private final PointsService pointsService;

    // @Autowired
    public PointsController(PointsService pointsService) {
        this.pointsService = pointsService;
    }

    // @GetMapping("/me")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object getCurrentUserPoints(
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            UserPointsDto userPoints = pointsService.getUserPoints(username);
            // return ResponseEntity.ok(userPoints);
            return userPoints;
        } catch (Exception e) { // Catch ResourceNotFoundException
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping("/me/history")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<PageResponse<PointTransactionDto>>*/ Object getCurrentUserPointHistory(
            /*@RequestParam(defaultValue = "0")*/ int page,
            /*@RequestParam(defaultValue = "10")*/ int size,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            PageResponse<PointTransactionDto> history = pointsService.getUserPointHistory(username, page, size);
            // return ResponseEntity.ok(history);
            return history;
        } catch (Exception e) { // Catch ResourceNotFoundException
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping("/leaderboard")
    // Consider if this should be public or authenticated
    public /*ResponseEntity<List<UserPointsDto>>*/ Object getLeaderboard(
            /*@RequestParam(defaultValue = "10")*/ int limit) {
        try {
            if (limit <= 0 || limit > 100) { // Basic validation for limit
                // return ResponseEntity.badRequest().body(Map.of("error", "Limit must be between 1 and 100."));
                return Map.of("error", "Limit must be between 1 and 100.");
            }
            List<UserPointsDto> leaderboard = pointsService.getLeaderboard(limit);
            // return ResponseEntity.ok(leaderboard);
            return leaderboard;
        } catch (Exception e) {
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error fetching leaderboard: " + e.getMessage()));
            return Map.of("error", "Error fetching leaderboard: " + e.getMessage());
        }
    }
    
    // Endpoint for manually triggering daily login points (for testing or specific scenarios)
    // In a real app, this might be part of the login flow itself in UserServiceImpl.
    // @PostMapping("/me/daily-login")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object triggerDailyLoginPoints(
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            pointsService.awardDailyLoginPoints(username);
            // return ResponseEntity.ok(Map.of("message", "Daily login points processed."));
            return Map.of("message", "Daily login points processed.");
        } catch (Exception e) {
             // return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }
}
