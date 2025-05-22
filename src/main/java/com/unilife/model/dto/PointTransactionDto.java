package com.unilife.model.dto;

import com.unilife.model.enums.PointActionType;
import java.time.LocalDateTime;

public class PointTransactionDto {
    private Long id;
    private Long userId;
    private String username; // Denormalized from User entity
    private int pointsAwarded;
    private PointActionType actionType;
    private String actionTypeDescription; // Description from enum
    private String description; // Custom description
    private Long relatedEntityId;
    private LocalDateTime transactionDate;

    // Constructors
    public PointTransactionDto() {
    }

    public PointTransactionDto(Long id, Long userId, String username, int pointsAwarded,
                               PointActionType actionType, String description, Long relatedEntityId,
                               LocalDateTime transactionDate) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.pointsAwarded = pointsAwarded;
        this.actionType = actionType;
        if (actionType != null) {
            this.actionTypeDescription = actionType.getDefaultDescription();
        }
        this.description = description;
        this.relatedEntityId = relatedEntityId;
        this.transactionDate = transactionDate;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPointsAwarded() {
        return pointsAwarded;
    }

    public void setPointsAwarded(int pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    public PointActionType getActionType() {
        return actionType;
    }

    public void setActionType(PointActionType actionType) {
        this.actionType = actionType;
        if (actionType != null) {
            this.actionTypeDescription = actionType.getDefaultDescription();
        }
    }

    public String getActionTypeDescription() {
        return actionTypeDescription;
    }

    public void setActionTypeDescription(String actionTypeDescription) {
        this.actionTypeDescription = actionTypeDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(Long relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "PointTransactionDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", pointsAwarded=" + pointsAwarded +
                ", actionType=" + actionType +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
