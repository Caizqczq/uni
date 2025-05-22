package com.unilife.service;

import com.unilife.model.dto.ReminderDto;
import java.time.LocalDateTime;
import java.util.List;

public interface ReminderService {
    List<ReminderDto> getPendingReminders(String username, LocalDateTime currentTime);
    void markEventReminderSent(Long eventId, String username);
    void markStudyGoalReminderSent(Long goalId, String username);
    void updateEventReminder(Long eventId, boolean enabled, LocalDateTime reminderTime, String username);
    void updateStudyGoalReminder(Long goalId, boolean enabled, LocalDateTime reminderTime, String username);
}
