package com.unilife.model.entity;

import java.time.LocalDateTime;
// import javax.persistence.*; // JPA annotations for reference

public class Comment {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Lob
    // @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Long userId;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    private Long postId;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "parent_comment_id")
    private Comment parentComment; // In code, but store parentCommentId in DB
    private Long parentCommentId; // Actual column in DB

    // @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Comment() {
        this.createdAt = LocalDateTime.now();
    }

    public Comment(String content, User user, Post post) {
        this();
        this.content = content;
        this.user = user;
        this.userId = user != null ? user.getId() : null;
        this.post = post;
        this.postId = post != null ? post.getId() : null;
    }

    public Comment(String content, User user, Post post, Comment parentComment) {
        this(content, user, post);
        this.parentComment = parentComment;
        this.parentCommentId = parentComment != null ? parentComment.getId() : null;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public Long getUserId() {
        if (this.userId == null && this.user != null) {
            return this.user.getId();
        }
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
        if (this.user != null && !this.user.getId().equals(userId)) {
            this.user = null;
        }
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
        this.postId = post != null ? post.getId() : null;
    }

    public Long getPostId() {
        if (this.postId == null && this.post != null) {
            return this.post.getId();
        }
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
        if (this.post != null && !this.post.getId().equals(postId)) {
            this.post = null;
        }
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
        this.parentCommentId = parentComment != null ? parentComment.getId() : null;
    }

    public Long getParentCommentId() {
        if (this.parentCommentId == null && this.parentComment != null) {
            return this.parentComment.getId();
        }
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
        if (this.parentComment != null && (parentCommentId == null || !this.parentComment.getId().equals(parentCommentId))) {
            this.parentComment = null;
        }
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + getUserId() +
                ", postId=" + getPostId() +
                ", parentCommentId=" + getParentCommentId() +
                ", createdAt=" + createdAt +
                '}';
    }
}
