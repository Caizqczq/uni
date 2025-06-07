package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object for displaying a pending reminder.")
public class ReminderDto {

    @Schema(description = "Composite ID for the reminder (e.g., 'event-123' or 'goal-456').", example = "event-123", accessMode = Schema.AccessMode.READ_ONLY)
    private String id; // Composite ID, e.g., "event-123"

    @Schema(description = "Type of the item this reminder is for (EVENT or GOAL).", example = "EVENT", accessMode = Schema.AccessMode.READ_ONLY)
    private String type; // "EVENT" or "GOAL"

    @Schema(description = "Title of the event or study goal.", example = "Team Meeting")
    private String title;

    @Schema(description = "Description of the event or study goal.", example = "Discuss project milestones.", nullable = true)
    private String description;

    @Schema(description = "Due time of the event or target date/time of the goal.", example = "2023-11-15T14:00:00")
    private LocalDateTime dueTime; // Event startTime or Goal targetDate (as LocalDateTime for consistency, date part for Goal)

    @Schema(description = "The time set for this reminder.", example = "2023-11-15T13:30:00")
    private LocalDateTime reminderTime;

    @Schema(description = "Original ID of the PersonalEvent or StudyGoal entity.", example = "123", accessMode = Schema.AccessMode.READ_ONLY)
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
