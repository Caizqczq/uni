package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// No validation annotations for now
@Schema(description = "Data Transfer Object for creating or updating a shared document.")
public class SharedDocumentRequestDto {

    @Schema(description = "Title of the shared document.", example = "Lecture Notes - Week 1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Content of the shared document (e.g., Markdown, plain text).", example = "# Chapter 1\n## Introduction...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "ID of the course this document is associated with.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
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
