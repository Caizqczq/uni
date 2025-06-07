package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object for displaying a comment, including author details.")
public class CommentResponseDto {

    @Schema(description = "Unique identifier of the comment.", example = "201", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Content of the comment.", example = "This is a great post!")
    private String content;

    @Schema(description = "Username of the comment author.", example = "jane.doe")
    private String authorUsername;

    @Schema(description = "Nickname of the comment author.", example = "Janie")
    private String authorNickname; // Added

    @Schema(description = "Avatar URL of the comment author.", example = "http://example.com/avatar_jane.jpg")
    private String authorAvatarUrl; // Added

    @Schema(description = "ID of the post this comment belongs to.", example = "101")
    private Long postId;

    @Schema(description = "ID of the parent comment if this is a reply. Null if it's a top-level comment.", example = "null", nullable = true)
    private Long parentCommentId; // Optional

    @Schema(description = "Timestamp of when the comment was created.", accessMode = Schema.AccessMode.READ_ONLY)
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
