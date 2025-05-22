package com.unilife.model.dto;

import java.time.LocalDateTime;

public class UserPointsDto {
    private Long userId;
    private String username; // Denormalized from User entity
    private int totalPoints;
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
