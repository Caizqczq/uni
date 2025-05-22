package com.unilife.model.entity;

import java.time.LocalDateTime;
// import javax.persistence.*; // JPA annotations for reference

public class SharedFile {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(nullable = false)
    private String fileName; // Original name

    // @Column(nullable = false, unique = true)
    private String storedFileName; // UUID + extension

    // @Column(nullable = false)
    private String filePath; // Relative path within upload directory

    // @Column(nullable = false)
    private String fileType; // MIME type

    // @Column(nullable = false)
    private Long fileSize; // In bytes

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "course_id", nullable = true) // Course is optional
    private CourseInfo course; // In code, but store courseId in DB
    private Long courseId; // Actual column in DB, nullable

    // @Column(name = "uploaded_by_user_id", nullable = false)
    private Long uploadedByUserId;

    // @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private String description; // Optional

    public SharedFile() {
        this.createdAt = LocalDateTime.now();
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

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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
        if (this.course != null && (courseId == null || !this.course.getId().equals(courseId))) {
            this.course = null;
        }
    }

    public Long getUploadedByUserId() {
        return uploadedByUserId;
    }

    public void setUploadedByUserId(Long uploadedByUserId) {
        this.uploadedByUserId = uploadedByUserId;
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
        return "SharedFile{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", storedFileName='" + storedFileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", courseId=" + getCourseId() +
                ", uploadedByUserId=" + uploadedByUserId +
                ", createdAt=" + createdAt +
                '}';
    }
}
