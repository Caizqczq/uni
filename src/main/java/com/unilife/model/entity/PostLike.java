package com.unilife.model.entity;

import java.time.LocalDateTime;
// import javax.persistence.*; // JPA annotations for reference

// @Entity
// @Table(name = "post_likes", uniqueConstraints = {
//    @UniqueConstraint(columnNames = {"user_id", "post_id"})
// })
public class PostLike {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Long userId;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    private Long postId;

    // @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public PostLike() {
        this.createdAt = LocalDateTime.now();
    }

    public PostLike(User user, Post post) {
        this();
        this.user = user;
        this.userId = user != null ? user.getId() : null;
        this.post = post;
        this.postId = post != null ? post.getId() : null;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "PostLike{" +
                "id=" + id +
                ", userId=" + getUserId() +
                ", postId=" + getPostId() +
                ", createdAt=" + createdAt +
                '}';
    }
}
