package com.unilife.controller;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.model.dto.ReminderDto;
import com.unilife.model.dto.UpdateReminderRequestDto;
import com.unilife.service.ReminderService;
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
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map; // For error/success messages
import org.springframework.http.HttpStatus; // For ResponseEntity
import org.springframework.http.ResponseEntity; // For ResponseEntity

// @RestController
// @RequestMapping("/api")
@Tag(name = "Reminder Management", description = "APIs for managing reminders for events and study goals.")
@SecurityRequirement(name = "bearerAuth")
public class ReminderController {

    private final ReminderService reminderService;

    // @Autowired
    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @Operation(summary = "Update reminder settings for a personal event", description = "Allows authenticated users to enable/disable or change the reminder time for their personal event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event reminder settings updated successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., reminder time not provided when enabling)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not own the event)"),
            @ApiResponse(responseCode = "404", description = "Event not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @PutMapping("/calendar/events/{eventId}/reminder")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object updateEventReminder(
            @Parameter(description = "ID of the personal event to update reminder for.", required = true, example = "1") /*@PathVariable*/ Long eventId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Reminder settings to apply.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = UpdateReminderRequestDto.class)))
            /*@RequestBody*/ UpdateReminderRequestDto reminderRequestDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            reminderService.updateEventReminder(eventId, reminderRequestDto.isEnabled(), reminderRequestDto.getReminderTime(), username);
            return ResponseEntity.ok(Map.of("message", "Event reminder settings updated successfully."));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Catch generic RuntimeException for AccessDenied placeholder
            if (e.getMessage() != null && e.getMessage().contains("User not authorized")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Update reminder settings for a study goal", description = "Allows authenticated users to enable/disable or change the reminder time for their study goal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Study goal reminder settings updated successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not own the goal)"),
            @ApiResponse(responseCode = "404", description = "Study goal not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @PutMapping("/study-plans/goals/{goalId}/reminder")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object updateStudyGoalReminder(
            @Parameter(description = "ID of the study goal to update reminder for.", required = true, example = "1") /*@PathVariable*/ Long goalId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Reminder settings to apply.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = UpdateReminderRequestDto.class)))
            /*@RequestBody*/ UpdateReminderRequestDto reminderRequestDto,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            reminderService.updateStudyGoalReminder(goalId, reminderRequestDto.isEnabled(), reminderRequestDto.getReminderTime(), username);
            return ResponseEntity.ok(Map.of("message", "Study goal reminder settings updated successfully."));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Catch generic RuntimeException for AccessDenied placeholder
            if (e.getMessage() != null && e.getMessage().contains("User not authorized")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get pending reminders for the user", description = "Retrieves a list of all pending reminders (events and study goals) for the authenticated user whose reminder time is due.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved pending reminders",
                         content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReminderDto.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // Optional: Endpoint to get pending reminders (for polling by client)
    // @GetMapping("/reminders/pending")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<List<ReminderDto>>*/ Object getPendingReminders(
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            LocalDateTime currentTime = LocalDateTime.now(); // Or from request param if needed
            List<ReminderDto> reminders = reminderService.getPendingReminders(username, currentTime);
            return ResponseEntity.ok(reminders);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    // The following endpoints for marking reminders as sent are typically called by a backend job/scheduler,
    // not directly by a user. However, they can be exposed for testing or specific admin scenarios.
    // If exposed, they should be heavily restricted (e.g., to system internal calls or specific admin roles).

    @Operation(summary = "Mark an event reminder as sent (Admin/System)", description = "Marks a specific personal event's reminder as sent. Typically for backend use.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event reminder marked as sent",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not have permission or own the event)"),
            @ApiResponse(responseCode = "404", description = "Event not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @PostMapping("/calendar/events/{eventId}/reminder/mark-sent")
    // @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')") // Example restriction
    public /*ResponseEntity<?>*/ Object markEventReminderSent(
            @Parameter(description = "ID of the personal event.", required = true, example = "1") /*@PathVariable*/ Long eventId,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) { // Or system principal
        try {
            // String username = currentUser.getUsername(); // Or a system username
            String username = "testUser"; // Placeholder
            reminderService.markEventReminderSent(eventId, username);
            return ResponseEntity.ok(Map.of("message", "Event reminder marked as sent."));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Catch generic RuntimeException for AccessDenied placeholder
            if (e.getMessage() != null && e.getMessage().contains("User not authorized")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Mark a study goal reminder as sent (Admin/System)", description = "Marks a specific study goal's reminder as sent. Typically for backend use.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Study goal reminder marked as sent",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user does not have permission or own the goal)"),
            @ApiResponse(responseCode = "404", description = "Study goal not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @PostMapping("/study-plans/goals/{goalId}/reminder/mark-sent")
    // @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')") // Example restriction
    public /*ResponseEntity<?>*/ Object markStudyGoalReminderSent(
            @Parameter(description = "ID of the study goal.", required = true, example = "1") /*@PathVariable*/ Long goalId,
            /*@AuthenticationPrincipal UserDetails*/ @Parameter(hidden = true) Object currentUser) { // Or system principal
        try {
            // String username = currentUser.getUsername(); // Or a system username
            String username = "testUser"; // Placeholder
            reminderService.markStudyGoalReminderSent(goalId, username);
            return ResponseEntity.ok(Map.of("message", "Study goal reminder marked as sent."));
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
