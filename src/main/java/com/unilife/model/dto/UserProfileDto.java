package com.unilife.model.dto;

public class UserProfileDto {
    private String username;
    private String email;
    private String nickname;
    private String avatarUrl;
    private String school;
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
