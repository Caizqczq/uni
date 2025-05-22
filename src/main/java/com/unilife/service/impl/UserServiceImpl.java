package com.unilife.service;

import com.unilife.mapper.UserMapper;
import com.unilife.model.dto.UserLoginDto;
import com.unilife.exception.InvalidTokenException;
import com.unilife.exception.ResourceNotFoundException;
import com.unilife.exception.UserAlreadyExistsException;
import com.unilife.exception.UserNotEnabledException;
import com.unilife.model.dto.UserProfileDto;
import com.unilife.mapper.VerificationTokenMapper;
import com.unilife.model.dto.UserRegistrationDto;
import com.unilife.model.entity.User;
import com.unilife.model.entity.VerificationToken;
import com.unilife.service.EmailService;
import com.unilife.utils.JwtUtil;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;

// @Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final Object /*PasswordEncoder*/ passwordEncoder;
    private final Object /*AuthenticationManager*/ authenticationManager;
    private final JwtUtil jwtUtil;
    private final VerificationTokenMapper verificationTokenMapper;
    private final EmailService emailService;

    // @Autowired
    public UserServiceImpl(UserMapper userMapper,
                           Object /*PasswordEncoder*/ passwordEncoder,
                           Object /*AuthenticationManager*/ authenticationManager,
                           JwtUtil jwtUtil,
                           VerificationTokenMapper verificationTokenMapper,
                           EmailService emailService) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.verificationTokenMapper = verificationTokenMapper;
        this.emailService = emailService;
    }

    @Override
    // @Transactional
    public User registerUser(UserRegistrationDto registrationDto) {
        if (userMapper.existsByUsername(registrationDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + registrationDto.getUsername());
        }
        if (userMapper.existsByEmail(registrationDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + registrationDto.getEmail());
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        // user.setPassword(this.passwordEncoder.encode(registrationDto.getPassword())); // Actual encoding
        user.setPassword("encoded_" + registrationDto.getPassword()); // Placeholder for actual PasswordEncoder interface
        user.setEmail(registrationDto.getEmail());
        user.setNickname(registrationDto.getNickname());
        user.setRegistrationDate(LocalDateTime.now());
        user.setEnabled(false); // User is disabled until email verification
        Set<String> defaultRoles = new HashSet<>();
        defaultRoles.add("USER"); // Default role
        user.setRoles(defaultRoles);

        userMapper.save(user); // Save user to get ID

        // Generate verification token
        String tokenString = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(tokenString, user);
        verificationTokenMapper.save(verificationToken);

        // Send verification email
        emailService.sendVerificationEmail(user, tokenString);

        return user;
    }

    @Override
    public Map<String, Object> loginUser(UserLoginDto loginDto) {
        // Authenticate user
        // Authentication authentication = authenticationManager.authenticate(
        //         new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword())
        // );
        // SecurityContextHolder.getContext().setAuthentication(authentication);
        // UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // For placeholder without actual AuthenticationManager:
        User user = findByUsernameOrEmail(loginDto.getUsernameOrEmail());
        if (user == null) { // Check if user exists
            throw new ResourceNotFoundException("User not found with identifier: " + loginDto.getUsernameOrEmail());
        }
        // Placeholder for password check - actual check is done by AuthenticationManager
        if (!user.getPassword().equals("encoded_" + loginDto.getPassword()) /* placeholder for passwordEncoder.matches */) {
            // This specific exception might be thrown by AuthenticationManager in a real setup
            throw new IllegalArgumentException("Invalid username/email or password.");
        }
        if (!user.isEnabled()) {
            throw new UserNotEnabledException("User account is not enabled. Please verify your email: " + user.getEmail());
        }
        // End placeholder section

        user.setLastLoginDate(LocalDateTime.now());
        userMapper.update(user);

        // Generate JWT token
        // String token = jwtUtil.generateToken(userDetails);
        // Create a dummy UserDetails object for JwtUtil.generateToken
        Object dummyUserDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
        String token = jwtUtil.generateToken(dummyUserDetails);


        UserProfileDto userProfileDto = new UserProfileDto(
                user.getUsername(), //userDetails.getUsername(),
                user.getEmail(),
                user.getNickname(),
                user.getAvatarUrl(),
                user.getSchool(),
                user.getStudentId()
        );

        Map<String, Object> loginResponse = new HashMap<>();
        loginResponse.put("token", token);
        loginResponse.put("userProfile", userProfileDto);

        return loginResponse;
    }

    @Override
    public UserProfileDto getUserProfile(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }
        return new UserProfileDto(
                user.getUsername(),
                user.getEmail(),
                user.getNickname(),
                user.getAvatarUrl(),
                user.getSchool(),
                user.getStudentId()
        );
    }

    @Override
    // @Transactional
    public User updateUserProfile(String username, UserProfileDto profileDto) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        // Update editable fields
        user.setNickname(profileDto.getNickname());
        user.setAvatarUrl(profileDto.getAvatarUrl());
        user.setSchool(profileDto.getSchool());
        user.setStudentId(profileDto.getStudentId());
        // Email and username are typically not updated here or require special handling

        userMapper.update(user);
        return user;
    }

    @Override
    // @Transactional
    public boolean verifyEmail(String tokenString) {
        VerificationToken verificationToken = verificationTokenMapper.findByToken(tokenString);

        if (verificationToken == null) {
            throw new InvalidTokenException("Invalid verification token: " + tokenString);
        }
        if (verificationToken.isExpired()) {
            verificationTokenMapper.deleteByToken(tokenString); // Clean up expired token
            throw new InvalidTokenException("Verification token has expired: " + tokenString);
        }

        User user = verificationToken.getUser();
        if (user == null) { // Should not happen if DB integrity is maintained
            verificationTokenMapper.deleteByToken(tokenString); // Clean up potentially orphaned token
            throw new ResourceNotFoundException("User associated with token " + tokenString + " not found.");
        }

        user.setEnabled(true);
        userMapper.update(user); // Assumes UserMapper has an update method that can set 'enabled'

        verificationTokenMapper.deleteByToken(tokenString); // Token is used, delete it
        return true;
    }

    @Override
    public void resendVerificationEmail(String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User with email " + email + " not found.");
        }
        if (user.isEnabled()) {
            throw new UserAlreadyExistsException("Account is already verified for email: " + email); // Or a more specific "AccountAlreadyVerifiedException"
        }

        // Delete any existing tokens for this user to avoid confusion / multiple valid tokens
        verificationTokenMapper.deleteByUserId(user.getId());

        // Generate new verification token
        String tokenString = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(tokenString, user);
        verificationTokenMapper.save(verificationToken);

        // Send new verification email
        emailService.sendVerificationEmail(user, tokenString);
    }

    // This private helper method is no longer used as email sending is delegated to EmailService
    // private void sendVerificationEmail(User user) { ... }

    private User findByUsernameOrEmail(String usernameOrEmail) {
        User user = userMapper.findByUsername(usernameOrEmail);
        if (user == null) {
            user = userMapper.findByEmail(usernameOrEmail);
        }
        // if (user == null) {
            // This method is private and typically called after a check or by AuthenticationManager
            // If called directly where user might not exist, ResourceNotFoundException could be thrown here too.
            // For now, assuming it's called when user is expected to exist or null is a valid intermediate state.
        // }
        return user;
    }
}
