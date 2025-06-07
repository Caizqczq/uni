package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object for displaying a forum post, including author and topic details.")
public class PostResponseDto {

    @Schema(description = "Unique identifier of the post.", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Title of the post.", example = "My First Post")
    private String title;

    @Schema(description = "Content of the post.", example = "This is the content of my first post.")
    private String content;

    @Schema(description = "Username of the post author.", example = "john.doe")
    private String authorUsername;

    @Schema(description = "Nickname of the post author.", example = "JohnnyD")
    private String authorNickname; // Added

    @Schema(description = "Avatar URL of the post author.", example = "http://example.com/avatar.jpg")
    private String authorAvatarUrl; // Added

    @Schema(description = "Name of the topic this post belongs to.", example = "General Discussion")
    private String topicName; // Or Long topicId / TopicDto topic

    @Schema(description = "Timestamp of when the post was created.", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp of when the post was last updated.", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    @Schema(description = "Number of likes this post has received.", example = "42")
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
