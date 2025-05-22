package com.unilife.model.entity;

import java.time.LocalDateTime;
// import javax.persistence.*; // JPA annotations for reference

public class CourseInfo {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(unique = true, nullable = false)
    private String courseCode;

    // @Column(nullable = false)
    private String courseName;

    private String description;

    // @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public CourseInfo() {
        this.createdAt = LocalDateTime.now();
    }

    public CourseInfo(String courseCode, String courseName, String description) {
        this();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CourseInfo{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
