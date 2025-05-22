package com.unilife.model.dto;

import java.util.List;

public class CourseRelatedResourcesDto {
    private Long courseId;
    private String courseName;
    private List<SharedDocumentResponseDto> sharedDocuments;
    private List<SharedFileResponseDto> sharedFiles;

    // Constructors
    public CourseRelatedResourcesDto() {
    }

    public CourseRelatedResourcesDto(Long courseId, String courseName, List<SharedDocumentResponseDto> sharedDocuments, List<SharedFileResponseDto> sharedFiles) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.sharedDocuments = sharedDocuments;
        this.sharedFiles = sharedFiles;
    }

    // Getters and Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<SharedDocumentResponseDto> getSharedDocuments() {
        return sharedDocuments;
    }

    public void setSharedDocuments(List<SharedDocumentResponseDto> sharedDocuments) {
        this.sharedDocuments = sharedDocuments;
    }

    public List<SharedFileResponseDto> getSharedFiles() {
        return sharedFiles;
    }

    public void setSharedFiles(List<SharedFileResponseDto> sharedFiles) {
        this.sharedFiles = sharedFiles;
    }

    @Override
    public String toString() {
        return "CourseRelatedResourcesDto{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", sharedDocumentsCount=" + (sharedDocuments != null ? sharedDocuments.size() : 0) +
                ", sharedFilesCount=" + (sharedFiles != null ? sharedFiles.size() : 0) +
                '}';
    }
}
