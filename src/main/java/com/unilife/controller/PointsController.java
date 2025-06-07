package com.unilife.controller;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.model.dto.PointTransactionDto;
import com.unilife.model.dto.UserPointsDto;
import com.unilife.service.PointsService;
import com.unilife.utils.PageResponse;
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
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // For error responses
import org.springframework.http.HttpStatus; // For ResponseEntity
import org.springframework.http.ResponseEntity; // For ResponseEntity

// @RestController
// @RequestMapping("/api/points")
@Tag(name = "Points System Management", description = "APIs for managing user points and viewing leaderboard.")
public class PointsController {

    private final PointsService pointsService;

    // @Autowired
    public PointsController(PointsService pointsService) {
        this.pointsService = pointsService;
    }

    @Operation(summary = "Get current user's points", description = "Retrieves the total points and last update time for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user points",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserPointsDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    // @GetMapping("/me")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object getCurrentUserPoints(
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            UserPointsDto userPoints = pointsService.getUserPoints(username);
            return ResponseEntity.ok(userPoints);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get current user's point transaction history", description = "Retrieves a paginated list of point transactions for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved point history",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))), // PageResponse<PointTransactionDto>
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    // @GetMapping("/me/history")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<PageResponse<PointTransactionDto>>*/ Object getCurrentUserPointHistory(
            @Parameter(description = "Page number (0-indexed).", example = "0") /*@RequestParam(defaultValue = "0")*/ int page,
            @Parameter(description = "Number of transactions per page.", example = "10") /*@RequestParam(defaultValue = "10")*/ int size,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            PageResponse<PointTransactionDto> history = pointsService.getUserPointHistory(username, page, size);
            return ResponseEntity.ok(history);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get points leaderboard", description = "Retrieves the top users by points. Publicly accessible or restricted as needed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved leaderboard",
                         content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserPointsDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid limit value",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/leaderboard")
    // Consider if this should be public or authenticated
    public /*ResponseEntity<List<UserPointsDto>>*/ Object getLeaderboard(
            @Parameter(description = "Number of top users to retrieve for the leaderboard.", example = "10")
            /*@RequestParam(defaultValue = "10")*/ int limit) {
        try {
            if (limit <= 0 || limit > 100) { // Basic validation for limit
                return ResponseEntity.badRequest().body(Map.of("error", "Limit must be between 1 and 100."));
            }
            List<UserPointsDto> leaderboard = pointsService.getLeaderboard(limit);
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error fetching leaderboard: " + e.getMessage()));
        }
    }

    @Operation(summary = "Trigger daily login points (Manual)", description = "Manually triggers the awarding of daily login points for the authenticated user. Typically, this logic is part of the main login process.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daily login points processed (either awarded or already awarded today)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    // Endpoint for manually triggering daily login points (for testing or specific scenarios)
    // In a real app, this might be part of the login flow itself in UserServiceImpl.
    // @PostMapping("/me/daily-login")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object triggerDailyLoginPoints(
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            pointsService.awardDailyLoginPoints(username);
            return ResponseEntity.ok(Map.of("message", "Daily login points processed."));
        } catch (ResourceNotFoundException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) { // Catch any other service layer exceptions, e.g. if points awarding failed unexpectedly
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}
