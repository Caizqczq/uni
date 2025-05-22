package com.unilife.service.impl;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.mapper.PersonalEventMapper;
import com.unilife.mapper.StudyGoalMapper;
import com.unilife.mapper.UserMapper;
import com.unilife.model.dto.ReminderDto;
import com.unilife.model.entity.PersonalEvent;
import com.unilife.model.entity.StudyGoal;
import com.unilife.model.entity.User;
import com.unilife.service.ReminderService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// @Service
public class ReminderServiceImpl implements ReminderService {

    private final PersonalEventMapper personalEventMapper;
    private final StudyGoalMapper studyGoalMapper;
    private final UserMapper userMapper;

    // @Autowired
    public ReminderServiceImpl(PersonalEventMapper personalEventMapper, StudyGoalMapper studyGoalMapper, UserMapper userMapper) {
        this.personalEventMapper = personalEventMapper;
        this.studyGoalMapper = studyGoalMapper;
        this.userMapper = userMapper;
    }

    @Override
    public List<ReminderDto> getPendingReminders(String username, LocalDateTime currentTime) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        List<ReminderDto> pendingReminders = new ArrayList<>();

        List<PersonalEvent> pendingEvents = personalEventMapper.findPendingReminders(currentTime, user.getId());
        for (PersonalEvent event : pendingEvents) {
            pendingReminders.add(new ReminderDto(
                    "event-" + event.getId(),
                    "EVENT",
                    event.getTitle(),
                    event.getDescription(),
                    event.getStartTime(),
                    event.getReminderTime(),
                    event.getId()
            ));
        }

        List<StudyGoal> pendingGoals = studyGoalMapper.findPendingReminders(currentTime, user.getId());
        for (StudyGoal goal : pendingGoals) {
            pendingReminders.add(new ReminderDto(
                    "goal-" + goal.getId(),
                    "GOAL",
                    goal.getGoalTitle(),
                    goal.getGoalDescription(),
                    goal.getTargetDate().atStartOfDay(), // Assuming targetDate is LocalDate
                    goal.getReminderTime(),
                    goal.getId()
            ));
        }
        return pendingReminders;
    }

    @Override
    // @Transactional
    public void markEventReminderSent(Long eventId, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) throw new ResourceNotFoundException("User not found: " + username);

        PersonalEvent event = personalEventMapper.findById(eventId);
        if (event == null) throw new ResourceNotFoundException("PersonalEvent not found with id: " + eventId);

        if (!Objects.equals(event.getUserId(), user.getId())) {
            // throw new AccessDeniedException("User not authorized to modify this event's reminder");
            throw new RuntimeException("User not authorized to modify this event's reminder (placeholder for AccessDeniedException)");
        }
        event.setReminderSent(true);
        personalEventMapper.update(event);
    }

    @Override
    // @Transactional
    public void markStudyGoalReminderSent(Long goalId, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) throw new ResourceNotFoundException("User not found: " + username);

        StudyGoal goal = studyGoalMapper.findRawById(goalId); // Use findRawById to get entity
        if (goal == null) throw new ResourceNotFoundException("StudyGoal not found with id: " + goalId);

        if (!Objects.equals(goal.getUserId(), user.getId())) {
            // throw new AccessDeniedException("User not authorized to modify this goal's reminder");
            throw new RuntimeException("User not authorized to modify this goal's reminder (placeholder for AccessDeniedException)");
        }
        goal.setReminderSent(true);
        studyGoalMapper.update(goal);
    }

    @Override
    // @Transactional
    public void updateEventReminder(Long eventId, boolean enabled, LocalDateTime reminderTime, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) throw new ResourceNotFoundException("User not found: " + username);

        PersonalEvent event = personalEventMapper.findById(eventId);
        if (event == null) throw new ResourceNotFoundException("PersonalEvent not found with id: " + eventId);

        if (!Objects.equals(event.getUserId(), user.getId())) {
            // throw new AccessDeniedException("User not authorized to update this event's reminder");
            throw new RuntimeException("User not authorized to update this event's reminder (placeholder for AccessDeniedException)");
        }

        if (enabled && reminderTime == null) {
            throw new IllegalArgumentException("Reminder time must be provided if reminder is enabled.");
        }
        if (enabled && reminderTime != null && reminderTime.isAfter(event.getStartTime())) {
            throw new IllegalArgumentException("Reminder time must be before the event start time.");
        }

        event.setReminderEnabled(enabled);
        event.setReminderTime(enabled ? reminderTime : null);
        event.setReminderSent(false); // Reset reminderSent status when settings change
        personalEventMapper.update(event);
    }

    @Override
    // @Transactional
    public void updateStudyGoalReminder(Long goalId, boolean enabled, LocalDateTime reminderTime, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) throw new ResourceNotFoundException("User not found: " + username);

        StudyGoal goal = studyGoalMapper.findRawById(goalId); // Fetch raw entity
        if (goal == null) throw new ResourceNotFoundException("StudyGoal not found with id: " + goalId);

        if (!Objects.equals(goal.getUserId(), user.getId())) {
            // throw new AccessDeniedException("User not authorized to update this goal's reminder");
            throw new RuntimeException("User not authorized to update this goal's reminder (placeholder for AccessDeniedException)");
        }

        if (enabled && reminderTime == null) {
            // Default reminder time for goals could be, e.g., 1 day before targetDate at 9 AM
            // For simplicity, require it if enabled, or implement default logic here.
            // This example assumes if reminderTime is null, it's an error or a default should be calculated.
            // If you want to allow enabling reminder without specific time (rely on default calculation), adjust this.
            throw new IllegalArgumentException("Reminder time must be provided if reminder is enabled for a study goal, or implement default calculation.");
        }
        if (enabled && reminderTime != null && reminderTime.isAfter(goal.getTargetDate().atTime(23, 59, 59))) { // Assuming targetDate is just a date
            throw new IllegalArgumentException("Reminder time must be before or on the goal's target date.");
        }

        goal.setReminderEnabled(enabled);
        goal.setReminderTime(enabled ? reminderTime : null);
        goal.setReminderSent(false); // Reset reminderSent status
        studyGoalMapper.update(goal);
    }
}
