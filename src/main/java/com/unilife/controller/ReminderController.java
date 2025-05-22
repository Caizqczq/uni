package com.unilife.controller;

import com.unilife.model.dto.ReminderDto;
import com.unilife.model.dto.UpdateReminderRequestDto;
import com.unilife.service.ReminderService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map; // For error/success messages

// @RestController
// @RequestMapping("/api")
public class ReminderController {

    private final ReminderService reminderService;

    // @Autowired
    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    // Endpoint to update reminder settings for a PersonalEvent
    // @PutMapping("/calendar/events/{eventId}/reminder")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object updateEventReminder(
            /*@PathVariable*/ Long eventId,
            /*@RequestBody*/ UpdateReminderRequestDto reminderRequestDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            reminderService.updateEventReminder(eventId, reminderRequestDto.isEnabled(), reminderRequestDto.getReminderTime(), username);
            // return ResponseEntity.ok(Map.of("message", "Event reminder settings updated successfully."));
            return Map.of("message", "Event reminder settings updated successfully.");
        } catch (Exception e) {
            // Handle specific exceptions like ResourceNotFoundException, AccessDeniedException, IllegalArgumentException
            // return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // Endpoint to update reminder settings for a StudyGoal
    // @PutMapping("/study-plans/goals/{goalId}/reminder")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<?>*/ Object updateStudyGoalReminder(
            /*@PathVariable*/ Long goalId,
            /*@RequestBody*/ UpdateReminderRequestDto reminderRequestDto,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            reminderService.updateStudyGoalReminder(goalId, reminderRequestDto.isEnabled(), reminderRequestDto.getReminderTime(), username);
            // return ResponseEntity.ok(Map.of("message", "Study goal reminder settings updated successfully."));
            return Map.of("message", "Study goal reminder settings updated successfully.");
        } catch (Exception e) {
            // Handle specific exceptions
            // return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // Optional: Endpoint to get pending reminders (for polling by client)
    // @GetMapping("/reminders/pending")
    // @PreAuthorize("isAuthenticated()")
    public /*ResponseEntity<List<ReminderDto>>*/ Object getPendingReminders(
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) {
        try {
            // String username = currentUser.getUsername();
            String username = "testUser"; // Placeholder
            LocalDateTime currentTime = LocalDateTime.now(); // Or from request param if needed
            List<ReminderDto> reminders = reminderService.getPendingReminders(username, currentTime);
            // return ResponseEntity.ok(reminders);
            return reminders;
        } catch (Exception e) {
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }
    
    // The following endpoints for marking reminders as sent are typically called by a backend job/scheduler,
    // not directly by a user. However, they can be exposed for testing or specific admin scenarios.
    // If exposed, they should be heavily restricted (e.g., to system internal calls or specific admin roles).

    // @PostMapping("/calendar/events/{eventId}/reminder/mark-sent")
    // @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')") // Example restriction
    public /*ResponseEntity<?>*/ Object markEventReminderSent(
            /*@PathVariable*/ Long eventId,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) { // Or system principal
        try {
            // String username = currentUser.getUsername(); // Or a system username
            String username = "testUser"; // Placeholder
            reminderService.markEventReminderSent(eventId, username);
            // return ResponseEntity.ok(Map.of("message", "Event reminder marked as sent."));
            return Map.of("message", "Event reminder marked as sent.");
        } catch (Exception e) {
            // return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @PostMapping("/study-plans/goals/{goalId}/reminder/mark-sent")
    // @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')") // Example restriction
    public /*ResponseEntity<?>*/ Object markStudyGoalReminderSent(
            /*@PathVariable*/ Long goalId,
            /*@AuthenticationPrincipal UserDetails*/ Object currentUser) { // Or system principal
        try {
            // String username = currentUser.getUsername(); // Or a system username
            String username = "testUser"; // Placeholder
            reminderService.markStudyGoalReminderSent(goalId, username);
            // return ResponseEntity.ok(Map.of("message", "Study goal reminder marked as sent."));
            return Map.of("message", "Study goal reminder marked as sent.");
        } catch (Exception e) {
            // return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }
}
