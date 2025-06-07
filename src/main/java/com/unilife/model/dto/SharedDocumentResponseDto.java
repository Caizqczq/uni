package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object for displaying a shared document, including course and user details.")
public class SharedDocumentResponseDto {

    @Schema(description = "Unique identifier of the shared document.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Title of the shared document.", example = "Lecture Notes - Week 1")
    private String title;

    @Schema(description = "Content of the shared document.", example = "# Chapter 1\n## Introduction...")
    private String content;

    @Schema(description = "Information about the course this document is associated with.")
    private CourseInfoDto courseInfo; // Embed CourseInfo DTO

    @Schema(description = "Username of the user who created the document.", example = "docCreator", accessMode = Schema.AccessMode.READ_ONLY)
    private String createdByUsername;

    @Schema(description = "Username of the user who last updated the document.", example = "docUpdater", accessMode = Schema.AccessMode.READ_ONLY)
    private String lastUpdatedByUsername;

    @Schema(description = "Timestamp of when the document was created.", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp of when the document was last updated.", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    @Schema(description = "Version number of the document.", example = "2", accessMode = Schema.AccessMode.READ_ONLY)
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
