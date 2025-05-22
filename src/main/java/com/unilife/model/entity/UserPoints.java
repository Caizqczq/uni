package com.unilife.model.entity;

import java.time.LocalDateTime;
// import javax.persistence.*; // JPA annotations for reference

public class UserPoints {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(nullable = false, unique = true)
    private Long userId; // FK to User

    // @Column(nullable = false)
    private int totalPoints = 0;

    // @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;

    public UserPoints() {
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public UserPoints(Long userId) {
        this();
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    // @PreUpdate
    public void onUpdate() {
        this.lastUpdatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "UserPoints{" +
                "id=" + id +
                ", userId=" + userId +
                ", totalPoints=" + totalPoints +
                ", lastUpdatedAt=" + lastUpdatedAt +
                '}';
    }
}
