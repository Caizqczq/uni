package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object for updating reminder settings for an event or study goal.")
public class UpdateReminderRequestDto {

    @Schema(description = "Whether the reminder should be enabled.", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean enabled;

    @Schema(description = "The specific time for the reminder. Required if 'enabled' is true. Should be before the event/goal due time.", example = "2023-12-14T09:00:00", nullable = true)
    private LocalDateTime reminderTime; // Optional, relevant if enabled is true

    // Constructors
    public UpdateReminderRequestDto() {
    }

    public UpdateReminderRequestDto(boolean enabled, LocalDateTime reminderTime) {
        this.enabled = enabled;
        this.reminderTime = reminderTime;
    }

    // Getters and Setters
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    @Override
    public String toString() {
        return "UpdateReminderRequestDto{" +
                "enabled=" + enabled +
                ", reminderTime=" + reminderTime +
                '}';
    }
}
