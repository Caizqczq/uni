package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object for news articles.")
public class NewsArticleDto {

    @Schema(description = "Unique identifier of the news article.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Title of the news article.", example = "Campus Reopens Next Week", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Full content of the news article.", example = "Detailed information about the campus reopening schedule and guidelines...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "Source of the news article (e.g., URL or publication name).", example = "Campus Official Website", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String source;

    @Schema(description = "Author of the news article, if different from the uploader.", example = "University PR Office", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String author;

    @Schema(description = "Timestamp of when the article was originally published.", example = "2023-10-26T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime publishedAt;

    @Schema(description = "Timestamp of when the article was added to the system.", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp of when the article was last updated in the system.", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    @Schema(description = "Username or nickname of the user who posted this article.", example = "adminUser", accessMode = Schema.AccessMode.READ_ONLY)
    private String postedBy; // Nickname or username of the User who posted

    // Constructors
    public NewsArticleDto() {
    }

    public NewsArticleDto(Long id, String title, String content, String source, String author,
                          LocalDateTime publishedAt, LocalDateTime createdAt, LocalDateTime updatedAt, String postedBy) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.source = source;
        this.author = author;
        this.publishedAt = publishedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.postedBy = postedBy;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
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

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    @Override
    public String toString() {
        return "NewsArticleDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", author='" + author + '\'' +
                ", publishedAt=" + publishedAt +
                ", postedBy='" + postedBy + '\'' +
                '}';
    }
}
