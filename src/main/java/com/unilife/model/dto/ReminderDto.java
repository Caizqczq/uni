package com.unilife.model.dto;

import java.time.LocalDateTime;

public class ReminderDto {
    private String id; // Composite ID, e.g., "event-123"
    private String type; // "EVENT" or "GOAL"
    private String title;
    private String description;
    private LocalDateTime dueTime; // Event startTime or Goal targetDate (as LocalDateTime for consistency, date part for Goal)
    private LocalDateTime reminderTime;
    private Long originalItemId; // ID of the original PersonalEvent or StudyGoal

    // Constructors
    public ReminderDto() {
    }

    public ReminderDto(String id, String type, String title, String description, LocalDateTime dueTime, LocalDateTime reminderTime, Long originalItemId) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.dueTime = dueTime;
        this.reminderTime = reminderTime;
        this.originalItemId = originalItemId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDueTime() {
        return dueTime;
    }

    public void setDueTime(LocalDateTime dueTime) {
        this.dueTime = dueTime;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public Long getOriginalItemId() {
        return originalItemId;
    }

    public void setOriginalItemId(Long originalItemId) {
        this.originalItemId = originalItemId;
    }

    @Override
    public String toString() {
        return "ReminderDto{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", dueTime=" + dueTime +
                ", reminderTime=" + reminderTime +
                ", originalItemId=" + originalItemId +
                '}';
    }
}
