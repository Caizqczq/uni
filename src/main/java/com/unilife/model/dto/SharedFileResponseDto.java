package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object for displaying shared file information.")
public class SharedFileResponseDto {

    @Schema(description = "Unique identifier of the shared file.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Original name of the file.", example = "syllabus.pdf")
    private String fileName;

    @Schema(description = "MIME type of the file.", example = "application/pdf")
    private String fileType;

    @Schema(description = "Size of the file in bytes.", example = "102400")
    private Long fileSize;

    @Schema(description = "Information about the course this file is associated with. Can be null if not associated with any specific course.", nullable = true)
    private CourseInfoDto courseInfo; // Can be null

    @Schema(description = "Username of the user who uploaded the file.", example = "fileUploader", accessMode = Schema.AccessMode.READ_ONLY)
    private String uploadedByUsername;

    @Schema(description = "Timestamp of when the file was uploaded.", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Description of the file.", example = "Syllabus for CS101 - Fall 2023", nullable = true)
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
