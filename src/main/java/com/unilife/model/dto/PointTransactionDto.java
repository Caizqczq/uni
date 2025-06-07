package com.unilife.model.dto;

import com.unilife.model.enums.PointActionType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object for point transaction history.")
public class PointTransactionDto {

    @Schema(description = "Unique identifier of the transaction.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "User ID associated with this transaction.", example = "100", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "Username of the user.", example = "pointUser", accessMode = Schema.AccessMode.READ_ONLY)
    private String username; // Denormalized from User entity

    @Schema(description = "Number of points awarded or deducted in this transaction.", example = "10")
    private int pointsAwarded;

    @Schema(description = "Type of action that resulted in this transaction.")
    private PointActionType actionType;

    @Schema(description = "Default description for the action type.", example = "Created a new post", accessMode = Schema.AccessMode.READ_ONLY)
    private String actionTypeDescription; // Description from enum

    @Schema(description = "Custom description for the transaction, if any.", example = "Bonus points for event participation", nullable = true)
    private String description; // Custom description

    @Schema(description = "ID of the entity related to this transaction (e.g., Post ID, Comment ID). Optional.", example = "101", nullable = true)
    private Long relatedEntityId;

    @Schema(description = "Timestamp of when the transaction occurred.", accessMode = Schema.AccessMode.READ_ONLY)
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
