package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object for user's points information.")
public class UserPointsDto {

    @Schema(description = "User ID.", example = "100", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "Username of the user.", example = "pointUser", accessMode = Schema.AccessMode.READ_ONLY)
    private String username; // Denormalized from User entity

    @Schema(description = "Total accumulated points for the user.", example = "1500")
    private int totalPoints;

    @Schema(description = "Timestamp of when the user's points were last updated.", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime lastUpdatedAt;

    // Constructors
    public UserPointsDto() {
    }

    public UserPointsDto(Long userId, String username, int totalPoints, LocalDateTime lastUpdatedAt) {
        this.userId = userId;
        this.username = username;
        this.totalPoints = totalPoints;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @Override
    public String toString() {
        return "UserPointsDto{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", totalPoints=" + totalPoints +
                ", lastUpdatedAt=" + lastUpdatedAt +
                '}';
    }
}
