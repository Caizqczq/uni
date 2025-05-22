package com.unilife.model.dto;

import java.time.LocalDateTime;

public class SharedFileResponseDto {
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private CourseInfoDto courseInfo; // Can be null
    private String uploadedByUsername;
    private LocalDateTime createdAt;
    private String description;

    // Constructors
    public SharedFileResponseDto() {
    }

    public SharedFileResponseDto(Long id, String fileName, String fileType, Long fileSize,
                                 CourseInfoDto courseInfo, String uploadedByUsername,
                                 LocalDateTime createdAt, String description) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.courseInfo = courseInfo;
        this.uploadedByUsername = uploadedByUsername;
        this.createdAt = createdAt;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public CourseInfoDto getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(CourseInfoDto courseInfo) {
        this.courseInfo = courseInfo;
    }

    public String getUploadedByUsername() {
        return uploadedByUsername;
    }

    public void setUploadedByUsername(String uploadedByUsername) {
        this.uploadedByUsername = uploadedByUsername;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SharedFileResponseDto{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", courseCode=" + (courseInfo != null ? courseInfo.getCourseCode() : "N/A") +
                ", uploadedByUsername='" + uploadedByUsername + '\'' +
                '}';
    }
}
