package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// No validation annotations for now
// The actual file will be a MultipartFile in the controller
@Schema(description = "Data Transfer Object for file upload metadata. The actual file is sent as a multipart/form-data part.")
public class SharedFileRequestDto {

    @Schema(description = "ID of the course to associate the file with. Optional.", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long courseId; // Optional

    @Schema(description = "Description of the file. Optional.", example = "Syllabus for CS101", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
