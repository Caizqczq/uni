package com.unilife.controller;

import com.unilife.model.dto.LoginResponseDto;
import com.unilife.model.dto.UserLoginDto;
import com.unilife.model.dto.UserProfileDto;
import com.unilife.model.dto.UserRegistrationDto;
import com.unilife.model.entity.User;
import com.unilife.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
// import javax.validation.Valid; // If using validation
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody /*@Valid*/ UserRegistrationDto registrationDto) {
        try {
            User user = userService.registerUser(registrationDto);
            // Consider returning a UserProfileDto or just a success message
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body("User registered successfully: " + user.getUsername() + ". Please check your email to verify your account.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody /*@Valid*/ UserLoginDto loginDto) {
        try {
            Map<String, Object> loginData = userService.loginUser(loginDto);
            String token = (String) loginData.get("token");
            UserProfileDto userProfile = (UserProfileDto) loginData.get("userProfile");
            return ResponseEntity.ok(new LoginResponseDto(token, userProfile));
        } catch (IllegalArgumentException e) { // Typically for bad credentials by Spring Security
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) { // For account not enabled etc.
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred during login."));
        }
    }

    @GetMapping("/profile/{username}")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public ResponseEntity<?> getUserProfile(@PathVariable String username) {
        try {
            UserProfileDto userProfile = userService.getUserProfile(username);
            return ResponseEntity.ok(userProfile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @PutMapping("/profile/{username}")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public ResponseEntity<?> updateUserProfile(@PathVariable String username, @RequestBody /*@Valid*/ UserProfileDto profileDto) {
        try {
            User updatedUser = userService.updateUserProfile(username, profileDto);
            UserProfileDto updatedProfileDto = new UserProfileDto( // Map back to DTO
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getNickname(),
                updatedUser.getAvatarUrl(),
                updatedUser.getSchool(),
                updatedUser.getStudentId()
            );
            return ResponseEntity.ok(updatedProfileDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam("token") String token) {
        try {
            boolean isVerified = userService.verifyEmail(token);
            if (isVerified) {
                return ResponseEntity.ok(Map.of("message", "Email verified successfully. You can now login."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid or expired verification token."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An error occurred during email verification."));
        }
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<Map<String, String>> resendVerificationEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Email must be provided."));
        }
        try {
            userService.resendVerificationEmail(email);
            return ResponseEntity.ok(Map.of("message", "Verification email resent. Please check your inbox."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) { // E.g. account already verified
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An error occurred while resending verification email."));
        }
    }
}
