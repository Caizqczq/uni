package com.unilife.model.entity;

import java.time.LocalDateTime;
// import javax.persistence.*; // JPA annotations for reference

public class Post {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(nullable = false)
    private String title;

    // @Lob // For TEXT type
    // @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", nullable = false)
    private User user; // In code, but store userId in DB
    private Long userId; // Actual column in DB

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic; // In code, but store topicId in DB
    private Long topicId; // Actual column in DB

    // @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // @Column(nullable = false)
    private LocalDateTime updatedAt;

    // @Column(nullable = false)
    private int likesCount = 0;

    public Post() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Post(String title, String content, User user, Topic topic) {
        this();
        this.title = title;
        this.content = content;
        this.user = user;
        this.userId = user != null ? user.getId() : null;
        this.topic = topic;
        this.topicId = topic != null ? topic.getId() : null;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public Long getUserId() {
        // If user object is present, ensure userId is synced, useful if only ID was set initially
        if (this.userId == null && this.user != null) {
            return this.user.getId();
        }
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
        if (this.user != null && !this.user.getId().equals(userId)) {
            this.user = null; // Or fetch user by this ID
        }
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
        this.topicId = topic != null ? topic.getId() : null;
    }

    public Long getTopicId() {
        if (this.topicId == null && this.topic != null) {
            return this.topic.getId();
        }
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
        if (this.topic != null && !this.topic.getId().equals(topicId)) {
            this.topic = null; // Or fetch topic by this ID
        }
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

    // @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", userId=" + getUserId() +
                ", topicId=" + getTopicId() +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", likesCount=" + likesCount +
                '}';
    }
}
