package com.unilife.model.dto;

import java.time.LocalDateTime;

public class SharedDocumentResponseDto {
    private Long id;
    private String title;
    private String content;
    private CourseInfoDto courseInfo; // Embed CourseInfo DTO
    private String createdByUsername;
    private String lastUpdatedByUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int version;

    // Constructors
    public SharedDocumentResponseDto() {
    }

    public SharedDocumentResponseDto(Long id, String title, String content, CourseInfoDto courseInfo,
                                     String createdByUsername, String lastUpdatedByUsername,
                                     LocalDateTime createdAt, LocalDateTime updatedAt, int version) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.courseInfo = courseInfo;
        this.createdByUsername = createdByUsername;
        this.lastUpdatedByUsername = lastUpdatedByUsername;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CourseInfoDto getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(CourseInfoDto courseInfo) {
        this.courseInfo = courseInfo;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public String getLastUpdatedByUsername() {
        return lastUpdatedByUsername;
    }

    public void setLastUpdatedByUsername(String lastUpdatedByUsername) {
        this.lastUpdatedByUsername = lastUpdatedByUsername;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "SharedDocumentResponseDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", courseCode=" + (courseInfo != null ? courseInfo.getCourseCode() : "null") +
                ", version=" + version +
                '}';
    }
}
