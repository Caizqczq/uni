package com.unilife.service;

import com.unilife.model.dto.UserLoginDto;
import com.unilife.model.dto.UserProfileDto;
import com.unilife.model.dto.UserRegistrationDto;
import com.unilife.model.entity.User;

import java.util.Map;

public interface UserService {
    User registerUser(UserRegistrationDto registrationDto);
    Map<String, Object> loginUser(UserLoginDto loginDto); // Returning a map for token and user profile
    UserProfileDto getUserProfile(String username);
    User updateUserProfile(String username, UserProfileDto profileDto);
    boolean verifyEmail(String token); // Returns true if verification is successful
    void resendVerificationEmail(String email);
    // Helper method, could be private or moved if not needed in interface
    // User findByUsernameOrEmail(String usernameOrEmail);
}
