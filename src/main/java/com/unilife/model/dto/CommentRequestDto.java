package com.unilife.model.dto;

// import javax.validation.constraints.NotBlank;
// import javax.validation.constraints.NotNull;

public class CommentRequestDto {

    // @NotBlank(message = "Content cannot be blank")
    private String content;

    // @NotNull(message = "Post ID cannot be null")
    private Long postId;

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
