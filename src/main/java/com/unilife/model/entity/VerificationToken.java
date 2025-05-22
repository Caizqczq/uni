package com.unilife.model.entity;

import java.time.LocalDateTime;

// import javax.persistence.*; // JPA annotations would be here if using JPA

public class VerificationToken {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(nullable = false, unique = true)
    private String token;

    // @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    // @JoinColumn(nullable = false, name = "user_id")
    private User user; // In a real JPA setup, this would be mapped

    // @Column(nullable = false)
    private LocalDateTime expiryDate;

    private static final int EXPIRATION_HOURS = 24;

    public VerificationToken() {
        super();
    }

    public VerificationToken(String token, User user) {
        this();
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION_HOURS);
    }

    private LocalDateTime calculateExpiryDate(int expiryTimeInHours) {
        return LocalDateTime.now().plusHours(expiryTimeInHours);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "VerificationToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", userId=" + (user != null ? user.getId() : "null") +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
