package com.unilife.model.dto;

import java.time.LocalDateTime;

public class UpdateReminderRequestDto {
    private boolean enabled;
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
