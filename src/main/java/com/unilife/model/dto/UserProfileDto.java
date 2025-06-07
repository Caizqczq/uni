package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for user profile information.")
public class UserProfileDto {

    @Schema(description = "Username of the user.", example = "john.doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "Email address of the user.", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Public nickname of the user.", example = "JohnnyD", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String nickname;

    @Schema(description = "URL to the user's avatar image.", example = "http://example.com/avatar.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String avatarUrl;

    @Schema(description = "School the user attends.", example = "Example University", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String school;

    @Schema(description = "Student ID of the user.", example = "S1234567", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String studentId;

    // Constructors
    public UserProfileDto() {
    }

    public UserProfileDto(String username, String email, String nickname, String avatarUrl, String school, String studentId) {
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.school = school;
        this.studentId = studentId;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "UserProfileDto{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", school='" + school + '\'' +
                ", studentId='" + studentId + '\'' +
                '}';
    }
}
