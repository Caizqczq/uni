package com.unilife.model.entity;

import com.unilife.model.enums.PointActionType; // Assuming PointActionType is an enum
import java.time.LocalDateTime;
// import javax.persistence.*; // JPA annotations for reference

public class PointTransaction {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(nullable = false)
    private Long userId; // FK to User

    // @Column(nullable = false)
    private int pointsAwarded;

    // @Enumerated(EnumType.STRING) // If storing enum name
    // @Column(nullable = false)
    private PointActionType actionType; // Enum type
    // Or private String actionType; // If storing as string

    private String description; // Optional

    private Long relatedEntityId; // Optional

    // @Column(nullable = false, updatable = false)
    private LocalDateTime transactionDate;

    public PointTransaction() {
        this.transactionDate = LocalDateTime.now();
    }

    public PointTransaction(Long userId, int pointsAwarded, PointActionType actionType, String description, Long relatedEntityId) {
        this();
        this.userId = userId;
        this.pointsAwarded = pointsAwarded;
        this.actionType = actionType;
        this.description = description;
        this.relatedEntityId = relatedEntityId;
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
        return "PointTransaction{" +
                "id=" + id +
                ", userId=" + userId +
                ", pointsAwarded=" + pointsAwarded +
                ", actionType=" + actionType +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
