package com.unilife.service.impl;

import com.unilife.exception.UserAlreadyExistsException;
import com.unilife.exception.UserNotEnabledException;
import com.unilife.exception.ResourceNotFoundException;
import com.unilife.exception.InvalidTokenException;
import com.unilife.mapper.UserMapper;
import com.unilife.mapper.VerificationTokenMapper;
import com.unilife.model.dto.UserLoginDto;
import com.unilife.model.dto.UserProfileDto;
import com.unilife.model.dto.UserRegistrationDto;
import com.unilife.model.entity.User;
import com.unilife.model.entity.VerificationToken;
import com.unilife.service.EmailService;
import com.unilife.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private VerificationTokenMapper verificationTokenMapper;

    @Mock
    private Object /*PasswordEncoder*/ passwordEncoder; // Using Object for placeholder

    @Mock
    private Object /*AuthenticationManager*/ authenticationManager; // Using Object for placeholder

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationDto registrationDto;
    private User user;

    @BeforeEach
    void setUp() {
        registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("testuser");
        registrationDto.setPassword("password123");
        registrationDto.setEmail("test@example.com");
        registrationDto.setNickname("Test Nickname");

        user = new User();
        user.setId(1L);
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setNickname(registrationDto.getNickname());
        user.setPassword("encodedPassword_" + registrationDto.getPassword()); // Simulating encoding
        user.setEnabled(false);
        user.setRegistrationDate(LocalDateTime.now());
    }

    @Test
    void registerUser_success() {
        when(userMapper.existsByUsername(anyString())).thenReturn(false);
        when(userMapper.existsByEmail(anyString())).thenReturn(false);
        // when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword"); // If PasswordEncoder was real
        doNothing().when(userMapper).save(any(User.class));
        doNothing().when(verificationTokenMapper).save(any(VerificationToken.class));
        doNothing().when(emailService).sendVerificationEmail(any(User.class), anyString());

        User registeredUser = userService.registerUser(registrationDto);

        assertNotNull(registeredUser);
        assertEquals(registrationDto.getUsername(), registeredUser.getUsername());
        // assertEquals("encodedPassword", registeredUser.getPassword()); // Check if encoder was used
        assertFalse(registeredUser.isEnabled());

        verify(userMapper).existsByUsername(registrationDto.getUsername());
        verify(userMapper).existsByEmail(registrationDto.getEmail());
        // verify(passwordEncoder).encode(registrationDto.getPassword());
        verify(userMapper).save(any(User.class)); // ArgumentCaptor could be used for more specific checks
        verify(verificationTokenMapper).save(any(VerificationToken.class));
        verify(emailService).sendVerificationEmail(any(User.class), anyString());
    }

    @Test
    void registerUser_usernameExists() {
        when(userMapper.existsByUsername(registrationDto.getUsername())).thenReturn(true);

        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(registrationDto);
        });
        assertEquals("Username already exists: " + registrationDto.getUsername(), exception.getMessage());

        verify(userMapper).existsByUsername(registrationDto.getUsername());
        verify(userMapper, never()).existsByEmail(anyString());
        verify(userMapper, never()).save(any(User.class));
    }

    @Test
    void registerUser_emailExists() {
        when(userMapper.existsByUsername(registrationDto.getUsername())).thenReturn(false);
        when(userMapper.existsByEmail(registrationDto.getEmail())).thenReturn(true);

        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(registrationDto);
        });
        assertEquals("Email already exists: " + registrationDto.getEmail(), exception.getMessage());

        verify(userMapper).existsByUsername(registrationDto.getUsername());
        verify(userMapper).existsByEmail(registrationDto.getEmail());
        verify(userMapper, never()).save(any(User.class));
    }

    @Test
    void loginUser_success() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setUsernameOrEmail(user.getUsername());
        loginDto.setPassword("password123"); // Raw password

        user.setEnabled(true); // User must be enabled

        // Mocking AuthenticationManager behavior (simplified)
        // Authentication successfulAuthentication = mock(Authentication.class);
        // when(authenticationManager.authenticate(any())).thenReturn(successfulAuthentication);
        // when(successfulAuthentication.getPrincipal()).thenReturn(new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList()));

        // Direct user fetching for placeholder logic in UserServiceImpl
        when(userMapper.findByUsername(loginDto.getUsernameOrEmail())).thenReturn(user);
        when(jwtUtil.generateToken(any())).thenReturn("dummy.jwt.token");
        doNothing().when(userMapper).update(any(User.class)); // For lastLoginDate update

        Map<String, Object> loginResponse = userService.loginUser(loginDto);

        assertNotNull(loginResponse);
        assertEquals("dummy.jwt.token", loginResponse.get("token"));
        assertTrue(loginResponse.get("userProfile") instanceof UserProfileDto);
        UserProfileDto profileDto = (UserProfileDto) loginResponse.get("userProfile");
        assertEquals(user.getUsername(), profileDto.getUsername());

        // verify(authenticationManager).authenticate(any());
        verify(jwtUtil).generateToken(any());
        verify(userMapper).update(user); // Check if lastLoginDate was updated
    }

    @Test
    void loginUser_userNotFound() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setUsernameOrEmail("nonexistentuser");
        loginDto.setPassword("password123");

        // when(authenticationManager.authenticate(any())).thenThrow(new UsernameNotFoundException("User not found"));
        // For direct fetching placeholder:
        when(userMapper.findByUsername(loginDto.getUsernameOrEmail())).thenReturn(null);
        when(userMapper.findByEmail(loginDto.getUsernameOrEmail())).thenReturn(null);


        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.loginUser(loginDto);
        });
        assertEquals("User not found with identifier: " + loginDto.getUsernameOrEmail(), exception.getMessage());
    }

    @Test
    void loginUser_userNotEnabled() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setUsernameOrEmail(user.getUsername());
        loginDto.setPassword("password123");
        user.setEnabled(false); // User is not enabled

        // Authentication successful, but user is not enabled
        // Authentication successfulAuthentication = mock(Authentication.class);
        // when(authenticationManager.authenticate(any())).thenReturn(successfulAuthentication);
        // when(successfulAuthentication.getPrincipal()).thenReturn(new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList()));
        // when(userMapper.findByUsername(user.getUsername())).thenReturn(user); // User fetched after authentication

        // For direct fetching placeholder:
        when(userMapper.findByUsername(loginDto.getUsernameOrEmail())).thenReturn(user);


        Exception exception = assertThrows(UserNotEnabledException.class, () -> {
            userService.loginUser(loginDto);
        });
        assertEquals("User account is not enabled. Please verify your email: " + user.getEmail(), exception.getMessage());
    }


    @Test
    void verifyEmail_success() {
        String tokenString = UUID.randomUUID().toString();
        VerificationToken token = new VerificationToken(tokenString, user);
        token.setExpiryDate(LocalDateTime.now().plusHours(1)); // Not expired

        when(verificationTokenMapper.findByToken(tokenString)).thenReturn(token);
        doNothing().when(userMapper).update(any(User.class));
        doNothing().when(verificationTokenMapper).deleteByToken(tokenString);

        boolean result = userService.verifyEmail(tokenString);

        assertTrue(result);
        assertTrue(user.isEnabled());
        verify(userMapper).update(user);
        verify(verificationTokenMapper).deleteByToken(tokenString);
    }

    @Test
    void verifyEmail_tokenNotFound() {
        String tokenString = "invalidToken";
        when(verificationTokenMapper.findByToken(tokenString)).thenReturn(null);

        Exception exception = assertThrows(InvalidTokenException.class, () -> {
            userService.verifyEmail(tokenString);
        });
        assertEquals("Invalid verification token: " + tokenString, exception.getMessage());
    }

    @Test
    void verifyEmail_tokenExpired() {
        String tokenString = UUID.randomUUID().toString();
        VerificationToken token = new VerificationToken(tokenString, user);
        token.setExpiryDate(LocalDateTime.now().minusHours(1)); // Expired

        when(verificationTokenMapper.findByToken(tokenString)).thenReturn(token);
        doNothing().when(verificationTokenMapper).deleteByToken(tokenString); // Ensure cleanup is called

        Exception exception = assertThrows(InvalidTokenException.class, () -> {
            userService.verifyEmail(tokenString);
        });
        assertEquals("Verification token has expired: " + tokenString, exception.getMessage());
        verify(verificationTokenMapper).deleteByToken(tokenString);
    }

    @Test
    void resendVerificationEmail_success() {
        user.setEnabled(false); // User is not yet enabled
        when(userMapper.findByEmail(user.getEmail())).thenReturn(user);
        doNothing().when(verificationTokenMapper).deleteByUserId(user.getId());
        doNothing().when(verificationTokenMapper).save(any(VerificationToken.class));
        doNothing().when(emailService).sendVerificationEmail(any(User.class), anyString());

        userService.resendVerificationEmail(user.getEmail());

        verify(verificationTokenMapper).deleteByUserId(user.getId());
        verify(verificationTokenMapper).save(any(VerificationToken.class));
        verify(emailService).sendVerificationEmail(eq(user), anyString());
    }

    @Test
    void resendVerificationEmail_userNotFound() {
        String nonExistentEmail = "nonexistent@example.com";
        when(userMapper.findByEmail(nonExistentEmail)).thenReturn(null);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.resendVerificationEmail(nonExistentEmail);
        });
        assertEquals("User with email " + nonExistentEmail + " not found.", exception.getMessage());
    }

    @Test
    void resendVerificationEmail_userAlreadyEnabled() {
        user.setEnabled(true); // User is already enabled
        when(userMapper.findByEmail(user.getEmail())).thenReturn(user);

        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> { // Or custom AccountAlreadyVerifiedException
            userService.resendVerificationEmail(user.getEmail());
        });
        assertEquals("Account is already verified for email: " + user.getEmail(), exception.getMessage());
    }

    @Test
    void getUserProfile_success() {
        when(userMapper.findByUsername(user.getUsername())).thenReturn(user);
        UserProfileDto profile = userService.getUserProfile(user.getUsername());
        assertNotNull(profile);
        assertEquals(user.getUsername(), profile.getUsername());
        assertEquals(user.getEmail(), profile.getEmail());
        verify(userMapper).findByUsername(user.getUsername());
    }

    @Test
    void getUserProfile_notFound() {
        String nonExistentUsername = "ghost";
        when(userMapper.findByUsername(nonExistentUsername)).thenReturn(null);
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserProfile(nonExistentUsername);
        });
        assertEquals("User not found: " + nonExistentUsername, exception.getMessage());
    }

    @Test
    void updateUserProfile_success() {
        UserProfileDto profileUpdateDto = new UserProfileDto();
        profileUpdateDto.setNickname("NewNick");
        profileUpdateDto.setSchool("NewSchool");

        when(userMapper.findByUsername(user.getUsername())).thenReturn(user);
        doNothing().when(userMapper).update(any(User.class));

        User updatedUser = userService.updateUserProfile(user.getUsername(), profileUpdateDto);

        assertNotNull(updatedUser);
        assertEquals("NewNick", updatedUser.getNickname());
        assertEquals("NewSchool", updatedUser.getSchool());
        verify(userMapper).update(user);
    }

    @Test
    void updateUserProfile_notFound() {
        String nonExistentUsername = "ghost";
        UserProfileDto profileUpdateDto = new UserProfileDto();
        when(userMapper.findByUsername(nonExistentUsername)).thenReturn(null);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUserProfile(nonExistentUsername, profileUpdateDto);
        });
        assertEquals("User not found: " + nonExistentUsername, exception.getMessage());
    }

}
