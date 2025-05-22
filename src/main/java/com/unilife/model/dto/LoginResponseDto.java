package com.unilife.model.dto;

public class LoginResponseDto {
    private String token;
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
