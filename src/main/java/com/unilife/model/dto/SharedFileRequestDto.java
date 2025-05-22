package com.unilife.model.dto;

// No validation annotations for now
// The actual file will be a MultipartFile in the controller
public class SharedFileRequestDto {
    private Long courseId; // Optional
    private String description; // Optional

    // Constructors
    public SharedFileRequestDto() {
    }

    public SharedFileRequestDto(Long courseId, String description) {
        this.courseId = courseId;
        this.description = description;
    }

    // Getters and Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SharedFileRequestDto{" +
                "courseId=" + courseId +
                ", description='" + description + '\'' +
                '}';
    }
}
