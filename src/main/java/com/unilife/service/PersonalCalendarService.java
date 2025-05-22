package com.unilife.service;

import com.unilife.model.dto.PersonalEventDto;
import java.time.LocalDateTime;
import java.util.List;

public interface PersonalCalendarService {
    PersonalEventDto addEvent(PersonalEventDto eventDto, String username);
    PersonalEventDto getEventById(Long eventId, String username);
    List<PersonalEventDto> getUserEvents(String username, LocalDateTime rangeStart, LocalDateTime rangeEnd);
    PersonalEventDto updateEvent(Long eventId, PersonalEventDto eventDto, String username);
    void deleteEvent(Long eventId, String username);
}
