package com.unilife.controller;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.model.dto.PersonalEventDto;
import com.unilife.service.PersonalCalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.http.HttpStatus; // For ResponseEntity
import org.springframework.http.ResponseEntity; // For ResponseEntity

// @RestController
// @RequestMapping("/api/calendar/events")
@Tag(name = "Personal Calendar Management", description = "APIs for managing personal calendar events.")
@SecurityRequirement(name = "bearerAuth")
public class PersonalCalendarController {

    private final PersonalCalendarService personalCalendarService;

    // @Autowired
    public PersonalCalendarController(PersonalCalendarService personalCalendarService) {
        this.personalCalendarService = personalCalendarService;
    }

    @Operation(summary = "Add a new personal event", description = "Allows authenticated users to add a new event to their personal calendar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonalEventDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., invalid time range, reminder settings)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    // @PostMapping
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object addEvent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the personal event to create.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = PersonalEventDto.class)))
            /*@RequestBody*/ PersonalEventDto eventDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder for actual authenticated username
            PersonalEventDto createdEvent = personalCalendarService.addEvent(eventDto, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get a specific personal event by ID", description = "Retrieves details of a specific personal event belonging to the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved event",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonalEventDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not own this event)"),
            @ApiResponse(responseCode = "404", description = "Event not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/{eventId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object getEventById(
            @Parameter(description = "ID of the personal event to retrieve.", required = true, example = "1") /*@PathVariable*/ Long eventId,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            PersonalEventDto event = personalCalendarService.getEventById(eventId, username);
            return ResponseEntity.ok(event);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Catch generic RuntimeException for AccessDenied placeholder
            if (e.getMessage() != null && e.getMessage().contains("User not authorized")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get user's personal events", description = "Retrieves a list of personal events for the authenticated user, optionally filtered by a date range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved events",
                         content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonalEventDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid date range",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<List<PersonalEventDto>>*/ Object getUserEvents(
            @Parameter(description = "Optional start of the date range to filter events (ISO DATE_TIME format).", example = "2023-11-01T00:00:00")
            /*@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)*/ LocalDateTime rangeStart,
            @Parameter(description = "Optional end of the date range to filter events (ISO DATE_TIME format).", example = "2023-11-30T23:59:59")
            /*@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)*/ LocalDateTime rangeEnd,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            List<PersonalEventDto> events = personalCalendarService.getUserEvents(username, rangeStart, rangeEnd);
            return ResponseEntity.ok(events);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Update a personal event", description = "Allows authenticated users to update an existing personal event they own.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event updated successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonalEventDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not own this event)"),
            @ApiResponse(responseCode = "404", description = "Event not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @PutMapping("/{eventId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object updateEvent(
            @Parameter(description = "ID of the personal event to update.", required = true, example = "1") /*@PathVariable*/ Long eventId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated details for the event.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = PersonalEventDto.class)))
            /*@RequestBody*/ PersonalEventDto eventDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            PersonalEventDto updatedEvent = personalCalendarService.updateEvent(eventId, eventDto, username);
            return ResponseEntity.ok(updatedEvent);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Catch generic RuntimeException for AccessDenied placeholder
            if (e.getMessage() != null && e.getMessage().contains("User not authorized")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Delete a personal event", description = "Allows authenticated users to delete a personal event they own.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Event deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not own this event)"),
            @ApiResponse(responseCode = "404", description = "Event not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @DeleteMapping("/{eventId}")
    // @PreAuthorize("isAuthenticated()") // Service layer handles ownership
    public /*ResponseEntity<?>*/ Object deleteEvent(
            @Parameter(description = "ID of the personal event to delete.", required = true, example = "1") /*@PathVariable*/ Long eventId,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            personalCalendarService.deleteEvent(eventId, username);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Catch generic RuntimeException for AccessDenied placeholder
            if (e.getMessage() != null && e.getMessage().contains("User not authorized")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}
