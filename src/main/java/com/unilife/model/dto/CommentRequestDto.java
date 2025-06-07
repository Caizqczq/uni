package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// import javax.validation.constraints.NotBlank;
// import javax.validation.constraints.NotNull;

@Schema(description = "Data Transfer Object for creating or updating a comment.")
public class CommentRequestDto {

    @Schema(description = "Content of the comment.", example = "This is a great post!", requiredMode = Schema.RequiredMode.REQUIRED)
    // @NotBlank(message = "Content cannot be blank")
    private String content;

    @Schema(description = "ID of the post this comment belongs to.", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    // @NotNull(message = "Post ID cannot be null")
    private Long postId;

    @Schema(description = "ID of the parent comment if this is a reply. Null if it's a top-level comment.", example = "201", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long parentCommentId; // Optional

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public String toString() {
        return "CommentRequestDto{" +
                "content='" + (content != null ? content.substring(0, Math.min(content.length(), 50)) + "..." : "null") + '\'' +
                ", postId=" + postId +
                ", parentCommentId=" + parentCommentId +
                '}';
    }
}
