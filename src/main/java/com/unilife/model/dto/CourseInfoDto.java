package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// No validation annotations for now, can be added later
@Schema(description = "Data Transfer Object for course information.")
public class CourseInfoDto {

    @Schema(description = "Unique identifier of the course.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Unique code for the course.", example = "CS101", requiredMode = Schema.RequiredMode.REQUIRED)
    private String courseCode;

    @Schema(description = "Full name of the course.", example = "Introduction to Computer Science", requiredMode = Schema.RequiredMode.REQUIRED)
    private String courseName;

    @Schema(description = "Detailed description of the course.", example = "Covers fundamental concepts of computer science.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    // Constructors
    public CourseInfoDto() {
    }

    public CourseInfoDto(Long id, String courseCode, String courseName, String description) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CourseInfoDto{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}
