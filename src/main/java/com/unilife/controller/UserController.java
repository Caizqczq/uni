package com.unilife.controller;

import com.unilife.model.dto.*; // Import all DTOs from the package
import com.unilife.model.dto.UserRegistrationDto;
import com.unilife.model.entity.User;
import com.unilife.service.UserService;
import com.unilife.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
// import javax.validation.Valid; // If using validation
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for user registration, login, profile management, and email verification.")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account. Email verification will be required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully. Verification email sent."),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., username/email already exists, invalid email format)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User registration details", required = true,
                                         content = @Content(schema = @Schema(implementation = UserRegistrationDto.class)))
                                         @RequestBody /*@Valid*/ UserRegistrationDto registrationDto) {
        try {
            User user = userService.registerUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body("User registered successfully: " + user.getUsername() + ". Please check your email to verify your account.");
        } catch (com.unilife.exception.UserAlreadyExistsException e) { // Catch specific exception
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @Operation(summary = "Login a user", description = "Authenticates a user and returns a JWT token along with user profile information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "User account not enabled (email not verified)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User login credentials", required = true,
                                       content = @Content(schema = @Schema(implementation = UserLoginDto.class)))
                                       @RequestBody /*@Valid*/ UserLoginDto loginDto) {
        try {
            Map<String, Object> loginData = userService.loginUser(loginDto);
            String token = (String) loginData.get("token");
            UserProfileDto userProfile = (UserProfileDto) loginData.get("userProfile");
            return ResponseEntity.ok(new LoginResponseDto(token, userProfile));
        } catch (com.unilife.exception.UserNotEnabledException e) {
             return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        } catch (com.unilife.exception.ResourceNotFoundException | IllegalArgumentException e) { // For bad credentials or user not found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred during login."));
        }
    }

    @Operation(summary = "Get user profile", description = "Retrieves the profile information for a given username. Requires authentication. Users can only get their own profile unless they are an ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized (not authenticated)"),
            @ApiResponse(responseCode = "403", description = "Forbidden (authenticated user trying to access another user's profile without ADMIN role)"),
            @ApiResponse(responseCode = "404", description = "User not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/profile/{username}")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public ResponseEntity<?> getUserProfile(@Parameter(description = "Username of the user to retrieve profile for", required = true) @PathVariable String username) {
        try {
            UserProfileDto userProfile = userService.getUserProfile(username);
            return ResponseEntity.ok(userProfile);
        } catch (com.unilife.exception.ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @Operation(summary = "Update user profile", description = "Updates the profile information for a given username. Requires authentication. Users can only update their own profile unless they are an ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated user profile",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @PutMapping("/profile/{username}")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public ResponseEntity<?> updateUserProfile(@Parameter(description = "Username of the user to update profile for", required = true) @PathVariable String username,
                                             @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated profile data", required = true,
                                                                                                   content = @Content(schema = @Schema(implementation = UserProfileDto.class)))
                                             @RequestBody /*@Valid*/ UserProfileDto profileDto) {
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
        } catch (com.unilife.exception.ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) { // For other validation errors from service
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @Operation(summary = "Verify user email", description = "Verifies a user's email address using a token sent to their email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email verified successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Invalid or expired verification token",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(@Parameter(description = "Verification token received via email", required = true) @RequestParam("token") String token) {
        try {
            boolean isVerified = userService.verifyEmail(token);
            if (isVerified) {
                return ResponseEntity.ok(Map.of("message", "Email verified successfully. You can now login."));
            } else {
                // This case might be covered by exceptions thrown from the service for specific token issues
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid or expired verification token (general)."));
            }
        } catch (com.unilife.exception.InvalidTokenException e) { // Specific exception for token issues
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An error occurred during email verification."));
        }
    }

    @Operation(summary = "Resend email verification token", description = "Resends the email verification token to the user's email address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification email resent successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., email not provided, account already verified)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "User with the given email not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @PostMapping("/resend-verification-email")
    public ResponseEntity<Map<String, String>> resendVerificationEmail(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Email address to resend verification to. Expects a JSON object like: {\"email\": \"user@example.com\"}",
                                                                   required = true, content = @Content(schema = @Schema(type = "object", properties = @io.swagger.v3.oas.annotations.media.SchemaProperty(name = "email", type = "string"))))
            @RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Email must be provided."));
        }
        try {
            userService.resendVerificationEmail(email);
            return ResponseEntity.ok(Map.of("message", "Verification email resent. Please check your inbox."));
        } catch (com.unilife.exception.ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (com.unilife.exception.UserAlreadyExistsException | IllegalStateException e) { // E.g. account already verified
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An error occurred while resending verification email."));
        }
    }
}
