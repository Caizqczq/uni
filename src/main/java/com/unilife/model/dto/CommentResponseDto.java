package com.unilife.model.dto;

import java.time.LocalDateTime;

public class CommentResponseDto {
    private Long id;
    private String content;
    private String authorUsername;
    private String authorNickname; // Added
    private String authorAvatarUrl; // Added
    private Long postId;
    private Long parentCommentId; // Optional
    private LocalDateTime createdAt;

    // Constructors
    public CommentResponseDto() {
    }

    public CommentResponseDto(Long id, String content, String authorUsername, String authorNickname, String authorAvatarUrl, Long postId, Long parentCommentId, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.authorUsername = authorUsername;
        this.authorNickname = authorNickname;
        this.authorAvatarUrl = authorAvatarUrl;
        this.postId = postId;
        this.parentCommentId = parentCommentId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CommentResponseDto{" +
                "id=" + id +
                ", authorUsername='" + authorUsername + '\'' +
                ", postId=" + postId +
                ", parentCommentId=" + parentCommentId +
                ", createdAt=" + createdAt +
                '}';
    }
}
