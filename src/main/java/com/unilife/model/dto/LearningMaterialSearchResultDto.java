package com.unilife.model.dto;

import com.unilife.utils.PageResponse;

public class LearningMaterialSearchResultDto {
    private String searchTerm;
    private Long courseIdFilter; // Can be null if not filtering by course
    private PageResponse<SharedDocumentResponseDto> documents;
    private PageResponse<SharedFileResponseDto> files;

    // Constructors
    public LearningMaterialSearchResultDto() {
    }

    public LearningMaterialSearchResultDto(String searchTerm, Long courseIdFilter,
                                           PageResponse<SharedDocumentResponseDto> documents,
                                           PageResponse<SharedFileResponseDto> files) {
        this.searchTerm = searchTerm;
        this.courseIdFilter = courseIdFilter;
        this.documents = documents;
        this.files = files;
    }

    // Getters and Setters
    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public Long getCourseIdFilter() {
        return courseIdFilter;
    }

    public void setCourseIdFilter(Long courseIdFilter) {
        this.courseIdFilter = courseIdFilter;
    }

    public PageResponse<SharedDocumentResponseDto> getDocuments() {
        return documents;
    }

    public void setDocuments(PageResponse<SharedDocumentResponseDto> documents) {
        this.documents = documents;
    }

    public PageResponse<SharedFileResponseDto> getFiles() {
        return files;
    }

    public void setFiles(PageResponse<SharedFileResponseDto> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "LearningMaterialSearchResultDto{" +
                "searchTerm='" + searchTerm + '\'' +
                ", courseIdFilter=" + courseIdFilter +
                ", documentsCount=" + (documents != null && documents.getContent() != null ? documents.getContent().size() : 0) +
                ", filesCount=" + (files != null && files.getContent() != null ? files.getContent().size() : 0) +
                '}';
    }
}
