package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object for study goals.")
public class StudyGoalDto {

    @Schema(description = "Unique identifier of the study goal.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "User ID this goal belongs to. Usually set by the system.", example = "100", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "ID of the course this goal is related to. Optional.", example = "1", nullable = true)
    private Long courseId;

    @Schema(description = "Name of the course this goal is related to. Read-only, populated if courseId is set.", example = "Advanced Calculus", accessMode = Schema.AccessMode.READ_ONLY, nullable = true)
    private String courseName; // Denormalized

    @Schema(description = "Title of the study goal.", example = "Complete Chapter 3 exercises", requiredMode = Schema.RequiredMode.REQUIRED)
    private String goalTitle;

    @Schema(description = "Detailed description of the study goal.", example = "Focus on problems 3.5 to 3.10.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String goalDescription;

    @Schema(description = "Target date for achieving the goal.", example = "2023-12-15", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate targetDate;

    @Schema(description = "Current status of the goal (e.g., PENDING, IN_PROGRESS, COMPLETED, CANCELLED).", example = "PENDING", defaultValue = "PENDING")
    private String status;

    @Schema(description = "Priority of the goal (e.g., 1-Low, 2-Medium, 3-High).", example = "2", defaultValue = "2")
    private int priority;

    @Schema(description = "Timestamp of when the goal was created.", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp of when the goal was last updated.", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    @Schema(description = "Flag indicating if a reminder is enabled for this goal.", example = "false", defaultValue = "false")
    private boolean reminderEnabled;

    @Schema(description = "Specific time for the reminder. Optional, relevant if reminderEnabled is true.", example = "2023-12-14T09:00:00", nullable = true)
    private LocalDateTime reminderTime;

    @Schema(description = "Flag indicating if the reminder has been sent.", example = "false", accessMode = Schema.AccessMode.READ_ONLY)
    private boolean reminderSent;

    // Constructors
    public StudyGoalDto() {
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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
        return "StudyGoalDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", goalTitle='" + goalTitle + '\'' +
                ", targetDate=" + targetDate +
                ", status='" + status + '\'' +
                '}';
    }
}
