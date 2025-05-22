package com.unilife.service.impl;

import com.unilife.model.entity.User;
import com.unilife.service.EmailService;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.stereotype.Service;

import java.text.MessageFormat;

// @Service
public class EmailServiceImpl implements EmailService {

    // For actual email sending (dependencies would be needed)
    // private final JavaMailSender mailSender;

    // @Value("${app.base-url}")
    private String appBaseUrl = "http://localhost:8080"; // Default or from properties

    // @Autowired
    // public EmailServiceImpl(JavaMailSender mailSender) {
    //     this.mailSender = mailSender;
    // }
    public EmailServiceImpl() { // Constructor for simulation without mailSender
    }


    @Override
    public void sendVerificationEmail(User user, String token) {
        String recipientAddress = user.getEmail();
        String subject = "UniLife Email Verification";
        String verificationUrl = appBaseUrl + "/api/users/verify-email?token=" + token;
        String messageBody = MessageFormat.format(
                "Dear {0},\n\nPlease click the link below to verify your email address and activate your UniLife account:\n{1}\n\nIf you did not request this, please ignore this email.\n\nThanks,\nThe UniLife Team",
                user.getNickname() != null ? user.getNickname() : user.getUsername(),
                verificationUrl
        );

        // Simulate email sending
        System.out.println("----------------------------------------------------");
        System.out.println("Sending Email (Simulation):");
        System.out.println("To: " + recipientAddress);
        System.out.println("From: noreply@unilife.com");
        System.out.println("Subject: " + subject);
        System.out.println("Body:\n" + messageBody);
        System.out.println("----------------------------------------------------");

        // Actual email sending code:
        /*
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(messageBody);
        email.setFrom("noreply@unilife.com"); // Set a 'from' address
        mailSender.send(email);
        */
    }

    @Override
    public void sendPasswordResetEmail(User user, String token) {
        String recipientAddress = user.getEmail();
        String subject = "UniLife Password Reset Request";
        String resetUrl = appBaseUrl + "/reset-password?token=" + token; // Example URL
        String messageBody = MessageFormat.format(
                "Dear {0},\n\nSomeone has requested a password reset for your UniLife account. If this was you, please click the link below to reset your password:\n{1}\n\nIf you did not request a password reset, please ignore this email.\n\nThanks,\nThe UniLife Team",
                user.getNickname() != null ? user.getNickname() : user.getUsername(),
                resetUrl
        );

        // Simulate email sending
        System.out.println("----------------------------------------------------");
        System.out.println("Sending Email (Simulation) - Password Reset:");
        System.out.println("To: " + recipientAddress);
        System.out.println("From: noreply@unilife.com");
        System.out.println("Subject: " + subject);
        System.out.println("Body:\n" + messageBody);
        System.out.println("----------------------------------------------------");

        // Actual email sending code:
        /*
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(messageBody);
        mailSender.send(email);
        */
    }

    // Setter for appBaseUrl if not using @Value (e.g., for testing)
    public void setAppBaseUrl(String appBaseUrl) {
        this.appBaseUrl = appBaseUrl;
    }
}
