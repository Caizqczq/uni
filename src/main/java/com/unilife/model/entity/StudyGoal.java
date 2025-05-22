package com.unilife.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
// import javax.persistence.*; // JPA annotations for reference

public class StudyGoal {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(nullable = false)
    private Long userId; // FK to User

    // @Column(nullable = true) // Course is optional
    private Long courseId; // FK to Course entity from schedule

    // @Column(nullable = false)
    private String goalTitle;

    // @Lob
    // @Column(columnDefinition = "TEXT")
    private String goalDescription;

    // @Column(nullable = false)
    private LocalDate targetDate;

    // @Column(nullable = false)
    private String status; // e.g., "PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED"

    private int priority; // e.g., 1-Low, 2-Medium, 3-High

    // @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // @Column(nullable = false)
    private LocalDateTime updatedAt;

    // @Column(nullable = false)
    private boolean reminderEnabled = false;

    private LocalDateTime reminderTime; // Optional

    // @Column(nullable = false)
    private boolean reminderSent = false;

    public StudyGoal() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "PENDING"; // Default status
        this.priority = 2; // Default to Medium priority
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

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getGoalTitle() {
        return goalTitle;
    }

    public void setGoalTitle(String goalTitle) {
        this.goalTitle = goalTitle;
    }

    public String getGoalDescription() {
        return goalDescription;
    }

    public void setGoalDescription(String goalDescription) {
        this.goalDescription = goalDescription;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isReminderEnabled() {
        return reminderEnabled;
    }

    public void setReminderEnabled(boolean reminderEnabled) {
        this.reminderEnabled = reminderEnabled;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }

    @Override
    public String toString() {
        return "StudyGoal{" +
                "id=" + id +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", goalTitle='" + goalTitle + '\'' +
                ", targetDate=" + targetDate +
                ", status='" + status + '\'' +
                ", priority=" + priority +
                '}';
    }
}
