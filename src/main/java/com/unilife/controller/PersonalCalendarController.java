package com.unilife.controller;

import com.unilife.model.dto.PersonalEventDto;
import com.unilife.service.PersonalCalendarService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.format.annotation.DateTimeFormat;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails; // Or your custom UserPrincipal
// import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map; // For error responses

// @RestController
// @RequestMapping("/api/calendar/events")
public class PersonalCalendarController {

    private final PersonalCalendarService personalCalendarService;

    // @Autowired
    public PersonalCalendarController(PersonalCalendarService personalCalendarService) {
        this.personalCalendarService = personalCalendarService;
    }

    // @PostMapping
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object addEvent(
            /*@RequestBody*/ PersonalEventDto eventDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            PersonalEventDto createdEvent = personalCalendarService.addEvent(eventDto, username);
            // return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
            return createdEvent;
        } catch (Exception e) { // Catch ResourceNotFoundException, IllegalArgumentException
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping("/{eventId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object getEventById(
            /*@PathVariable*/ Long eventId,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            PersonalEventDto event = personalCalendarService.getEventById(eventId, username);
            // return ResponseEntity.ok(event);
            return event;
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<List<PersonalEventDto>>*/ Object getUserEvents(
            /*@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)*/ LocalDateTime rangeStart,
            /*@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)*/ LocalDateTime rangeEnd,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            List<PersonalEventDto> events = personalCalendarService.getUserEvents(username, rangeStart, rangeEnd);
            // return ResponseEntity.ok(events);
            return events;
        } catch (Exception e) { // Catch ResourceNotFoundException, IllegalArgumentException
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @PutMapping("/{eventId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object updateEvent(
            /*@PathVariable*/ Long eventId,
            /*@RequestBody*/ PersonalEventDto eventDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            PersonalEventDto updatedEvent = personalCalendarService.updateEvent(eventId, eventDto, username);
            // return ResponseEntity.ok(updatedEvent);
            return updatedEvent;
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException, IllegalArgumentException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @DeleteMapping("/{eventId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object deleteEvent(
            /*@PathVariable*/ Long eventId,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            personalCalendarService.deleteEvent(eventId, username);
            // return ResponseEntity.noContent().build();
            return Map.of("message", "Personal event deleted successfully");
        } catch (Exception e) { // Catch ResourceNotFoundException, AccessDeniedException
            // if (e instanceof ResourceNotFoundException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // if (e instanceof AccessDeniedException) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }
}
