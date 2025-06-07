package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object for personal calendar events.")
public class PersonalEventDto {

    @Schema(description = "Unique identifier of the personal event.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "User ID this event belongs to. Usually set by the system.", example = "100", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "Title of the event.", example = "Team Meeting", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Detailed description of the event.", example = "Discuss project milestones.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    @Schema(description = "Start time of the event.", example = "2023-11-15T14:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime startTime;

    @Schema(description = "End time of the event.", example = "2023-11-15T15:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime endTime;

    @Schema(description = "Location of the event.", example = "Conference Room B", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String location;

    @Schema(description = "Flag indicating if this is an all-day event.", example = "false", defaultValue = "false")
    private boolean isAllDay;

    @Schema(description = "Color code (hex) for displaying the event.", example = "#33AFFF", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String color;

    @Schema(description = "Flag indicating if a reminder is enabled for this event.", example = "true", defaultValue = "false")
    private boolean reminderEnabled;

    @Schema(description = "Specific time for the reminder. Required if reminderEnabled is true.", example = "2023-11-15T13:30:00", nullable = true)
    private LocalDateTime reminderTime;

    @Schema(description = "Flag indicating if the reminder has been sent.", example = "false", accessMode = Schema.AccessMode.READ_ONLY)
    private boolean reminderSent; // New field

    // Constructors
    public PersonalEventDto() {
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    public void setAllDay(boolean allDay) {
        isAllDay = allDay;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
        return "PersonalEventDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
