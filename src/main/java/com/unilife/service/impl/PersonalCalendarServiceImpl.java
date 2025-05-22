package com.unilife.service.impl;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.mapper.PersonalEventMapper;
import com.unilife.mapper.UserMapper;
import com.unilife.model.dto.PersonalEventDto;
import com.unilife.model.entity.PersonalEvent;
import com.unilife.model.entity.User;
import com.unilife.service.PersonalCalendarService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// @Service
public class PersonalCalendarServiceImpl implements PersonalCalendarService {

    private final PersonalEventMapper personalEventMapper;
    private final UserMapper userMapper;

    // @Autowired
    public PersonalCalendarServiceImpl(PersonalEventMapper personalEventMapper, UserMapper userMapper) {
        this.personalEventMapper = personalEventMapper;
        this.userMapper = userMapper;
    }

    @Override
    // @Transactional
    public PersonalEventDto addEvent(PersonalEventDto eventDto, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }

        // Basic validation
        if (eventDto.getStartTime() == null || eventDto.getEndTime() == null ||
            eventDto.getStartTime().isAfter(eventDto.getEndTime())) {
            throw new IllegalArgumentException("Invalid event times. Start time must be before or equal to end time.");
        }
        if (eventDto.isReminderEnabled() && eventDto.getReminderTime() == null) {
            throw new IllegalArgumentException("Reminder time must be set if reminder is enabled.");
        }
        if (eventDto.isReminderEnabled() && eventDto.getReminderTime() != null && eventDto.getReminderTime().isAfter(eventDto.getStartTime())) {
            throw new IllegalArgumentException("Reminder time must be before the event start time.");
        }


        PersonalEvent event = mapToEntity(eventDto);
        event.setUserId(user.getId());

        personalEventMapper.save(event);
        return mapToDto(event);
    }

    @Override
    public PersonalEventDto getEventById(Long eventId, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        PersonalEvent event = personalEventMapper.findById(eventId);
        if (event == null) {
            throw new ResourceNotFoundException("PersonalEvent not found with id: " + eventId);
        }
        if (!Objects.equals(event.getUserId(), user.getId())) {
            // throw new AccessDeniedException("User not authorized to access this event");
            throw new RuntimeException("User not authorized to access this event (placeholder for AccessDeniedException)");
        }
        return mapToDto(event);
    }

    @Override
    public List<PersonalEventDto> getUserEvents(String username, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        List<PersonalEvent> events;
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                 throw new IllegalArgumentException("Range start time must be before or equal to range end time.");
            }
            events = personalEventMapper.findByUserIdAndDateRange(user.getId(), rangeStart, rangeEnd);
        } else {
            events = personalEventMapper.findByUserId(user.getId());
        }
        return events.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    // @Transactional
    public PersonalEventDto updateEvent(Long eventId, PersonalEventDto eventDto, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        PersonalEvent existingEvent = personalEventMapper.findById(eventId);
        if (existingEvent == null) {
            throw new ResourceNotFoundException("PersonalEvent not found with id: " + eventId);
        }
        if (!Objects.equals(existingEvent.getUserId(), user.getId())) {
            // throw new AccessDeniedException("User not authorized to update this event");
            throw new RuntimeException("User not authorized to update this event (placeholder for AccessDeniedException)");
        }
        
        // Basic validation for update
        if (eventDto.getStartTime() != null && eventDto.getEndTime() != null &&
            eventDto.getStartTime().isAfter(eventDto.getEndTime())) {
            throw new IllegalArgumentException("Invalid event times. Start time must be before or equal to end time.");
        }
        // If only one of start/end time is provided, compare with existing one
        LocalDateTime newStartTime = eventDto.getStartTime() != null ? eventDto.getStartTime() : existingEvent.getStartTime();
        LocalDateTime newEndTime = eventDto.getEndTime() != null ? eventDto.getEndTime() : existingEvent.getEndTime();
        if (newStartTime.isAfter(newEndTime)) {
             throw new IllegalArgumentException("Invalid event times. Start time must be before or equal to end time considering existing values.");
        }
        
        boolean reminderEnabled = eventDto.isReminderEnabled(); // Check DTO's explicit value
        LocalDateTime reminderTime = eventDto.getReminderTime();

        if (reminderEnabled && reminderTime == null) {
            throw new IllegalArgumentException("Reminder time must be set if reminder is enabled.");
        }
        if (reminderEnabled && reminderTime != null && reminderTime.isAfter(newStartTime)) {
             throw new IllegalArgumentException("Reminder time must be before the event start time.");
        }


        updateEntityFromDto(existingEvent, eventDto);
        existingEvent.setUserId(user.getId()); // Ensure userId is not changed from DTO

        personalEventMapper.update(existingEvent);
        return mapToDto(existingEvent);
    }

    @Override
    // @Transactional
    public void deleteEvent(Long eventId, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        PersonalEvent event = personalEventMapper.findById(eventId);
        if (event == null) {
            // Optional: throw ResourceNotFoundException, or allow silent fail
            return; // Or throw new ResourceNotFoundException("PersonalEvent not found with id: " + eventId);
        }
        if (!Objects.equals(event.getUserId(), user.getId())) {
            // throw new AccessDeniedException("User not authorized to delete this event");
            throw new RuntimeException("User not authorized to delete this event (placeholder for AccessDeniedException)");
        }
        personalEventMapper.deleteByIdAndUserId(eventId, user.getId());
    }

    // Helper methods for mapping
    private PersonalEventDto mapToDto(PersonalEvent event) {
        if (event == null) return null;
        PersonalEventDto dto = new PersonalEventDto();
        dto.setId(event.getId());
        dto.setUserId(event.getUserId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setLocation(event.getLocation());
        dto.setAllDay(event.isAllDay());
        dto.setColor(event.getColor());
        dto.setReminderEnabled(event.isReminderEnabled());
        dto.setReminderTime(event.getReminderTime());
        return dto;
    }

    private PersonalEvent mapToEntity(PersonalEventDto dto) {
        if (dto == null) return null;
        PersonalEvent event = new PersonalEvent();
        // ID is not set from DTO for creation
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setLocation(dto.getLocation());
        event.setAllDay(dto.isAllDay());
        event.setColor(dto.getColor());
        event.setReminderEnabled(dto.isReminderEnabled());
        event.setReminderTime(dto.getReminderTime());
        // userId is set in the service method
        return event;
    }
    
    private void updateEntityFromDto(PersonalEvent event, PersonalEventDto dto) {
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getStartTime() != null) event.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) event.setEndTime(dto.getEndTime());
        if (dto.getLocation() != null) event.setLocation(dto.getLocation());
        event.setAllDay(dto.isAllDay()); // Boolean, always update from DTO
        if (dto.getColor() != null) event.setColor(dto.getColor());
        event.setReminderEnabled(dto.isReminderEnabled()); // Boolean
        // Only set reminder time if reminder is enabled, otherwise it should be null (or handled by DB default/trigger)
        event.setReminderTime(dto.isReminderEnabled() ? dto.getReminderTime() : null);
    }
}
