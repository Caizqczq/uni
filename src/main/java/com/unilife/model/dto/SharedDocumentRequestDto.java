package com.unilife.model.dto;

// No validation annotations for now
public class SharedDocumentRequestDto {
    private String title;
    private String content;
    private Long courseId;

    // Constructors
    public SharedDocumentRequestDto() {
    }

    public SharedDocumentRequestDto(String title, String content, Long courseId) {
        this.title = title;
        this.content = content;
        this.courseId = courseId;
    }

    // Getters and Setters
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

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "SharedDocumentRequestDto{" +
                "title='" + title + '\'' +
                ", courseId=" + courseId +
                '}';
    }
}
