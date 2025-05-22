package com.unilife.service;

import com.unilife.model.entity.User;

public interface EmailService {
    void sendVerificationEmail(User user, String token);
    void sendPasswordResetEmail(User user, String token); // Added for potential future use
}
