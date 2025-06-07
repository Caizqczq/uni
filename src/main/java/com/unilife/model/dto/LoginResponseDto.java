package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for login response, including JWT and user profile.")
public class LoginResponseDto {

    @Schema(description = "JSON Web Token (JWT) for authentication.", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImV4cCI6MTYxNjQwNjQwMCwiaWF0IjoxNjE2MzYwMDAwfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;

    @Schema(description = "User profile information of the logged-in user.", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserProfileDto userProfile;

    public LoginResponseDto(String token, UserProfileDto userProfile) {
        this.token = token;
        this.userProfile = userProfile;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserProfileDto getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileDto userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public String toString() {
        return "LoginResponseDto{" +
                "token='" + token + '\'' +
                ", userProfile=" + userProfile +
                '}';
    }
}
