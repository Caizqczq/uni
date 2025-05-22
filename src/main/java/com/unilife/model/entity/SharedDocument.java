package com.unilife.model.entity;

import java.time.LocalDateTime;
// import javax.persistence.*; // JPA annotations for reference

public class SharedDocument {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(nullable = false)
    private String title;

    // @Lob
    // @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "course_id", nullable = false)
    private CourseInfo course; // In code, but store courseId in DB
    private Long courseId; // Actual column in DB

    // @Column(name = "created_by_user_id", nullable = false)
    private Long createdByUserId;

    // @Column(name = "last_updated_by_user_id", nullable = false)
    private Long lastUpdatedByUserId;

    // @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // @Column(nullable = false)
    private LocalDateTime updatedAt;

    // @Column(nullable = false)
    private int version = 1; // Default to version 1

    public SharedDocument() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public CourseInfo getCourse() {
        return course;
    }

    public void setCourse(CourseInfo course) {
        this.course = course;
        this.courseId = (course != null) ? course.getId() : null;
    }

    public Long getCourseId() {
        if (this.courseId == null && this.course != null) {
            return this.course.getId();
        }
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
        if (this.course != null && !this.course.getId().equals(courseId)) {
            this.course = null; // Or fetch course by this ID
        }
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Long getLastUpdatedByUserId() {
        return lastUpdatedByUserId;
    }

    public void setLastUpdatedByUserId(Long lastUpdatedByUserId) {
        this.lastUpdatedByUserId = lastUpdatedByUserId;
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

    // @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.version++; // Increment version on update
    }

    @Override
    public String toString() {
        return "SharedDocument{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", courseId=" + getCourseId() +
                ", createdByUserId=" + createdByUserId +
                ", lastUpdatedByUserId=" + lastUpdatedByUserId +
                ", version=" + version +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
