package com.unilife.model.dto;

import java.time.LocalDateTime;

public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private String authorNickname; // Added
    private String authorAvatarUrl; // Added
    private String topicName; // Or Long topicId / TopicDto topic
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likesCount;

    // Constructors
    public PostResponseDto() {
    }

    public PostResponseDto(Long id, String title, String content, String authorUsername, String authorNickname, String authorAvatarUrl, String topicName, LocalDateTime createdAt, LocalDateTime updatedAt, int likesCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorUsername = authorUsername;
        this.authorNickname = authorNickname;
        this.authorAvatarUrl = authorAvatarUrl;
        this.topicName = topicName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.likesCount = likesCount;
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

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public void setAuthorNickname(String authorNickname) {
        this.authorNickname = authorNickname;
    }

    public String getAuthorAvatarUrl() {
        return authorAvatarUrl;
    }

    public void setAuthorAvatarUrl(String authorAvatarUrl) {
        this.authorAvatarUrl = authorAvatarUrl;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
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

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    @Override
    public String toString() {
        return "PostResponseDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authorUsername='" + authorUsername + '\'' +
                ", topicName='" + topicName + '\'' +
                ", likesCount=" + likesCount +
                ", createdAt=" + createdAt +
                '}';
    }
}
