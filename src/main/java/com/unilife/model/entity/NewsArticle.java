package com.unilife.model.entity;

import java.time.LocalDateTime;
// import javax.persistence.*; // JPA annotations for reference

public class NewsArticle {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(nullable = false)
    private String title;

    // @Lob
    // @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String source; // Optional
    private String author; // Optional, if manually entered

    // @Column(nullable = false)
    private LocalDateTime publishedAt;

    // @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // @Column(nullable = false)
    private LocalDateTime updatedAt;

    // @Column(name = "user_id") // FK to User
    private Long userId; // User who added/edited this article

    public NewsArticle() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.publishedAt = this.createdAt; // Default publishedAt to createdAt
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "NewsArticle{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", author='" + author + '\'' +
                ", publishedAt=" + publishedAt +
                ", userId=" + userId +
                '}';
    }
}
