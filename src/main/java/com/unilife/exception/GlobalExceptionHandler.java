package com.unilife.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildErrorResponse(Exception ex, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", "")); // Basic path info
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request); // 409 Conflict
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request); // 400 Bad Request
    }

    @ExceptionHandler(UserNotEnabledException.class)
    public ResponseEntity<Object> handleUserNotEnabledException(UserNotEnabledException ex, WebRequest request) {
        // Could be 401 Unauthorized or 403 Forbidden depending on context.
        // 403 is often used if the user is authenticated but not authorized for the action (e.g. account disabled)
        return buildErrorResponse(ex, HttpStatus.FORBIDDEN, request); // 403 Forbidden
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request); // 404 Not Found
    }

    // Spring Security exceptions (add if not handled by default or if custom responses are needed)
    // @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    // public ResponseEntity<Object> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex, WebRequest request) {
    //     return buildErrorResponse(ex, HttpStatus.FORBIDDEN, request);
    // }

    // @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    // public ResponseEntity<Object> handleBadCredentialsException(org.springframework.security.authentication.BadCredentialsException ex, WebRequest request) {
    //     return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, request);
    // }

    // Fallback for other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        // Log the exception for server-side tracking
        // logger.error("Unexpected error occurred: ", ex);
        System.err.println("Unexpected error occurred: " + ex.getMessage()); // Basic logging
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request); // 500 Internal Server Error
    }
}
