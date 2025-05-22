package com.unilife.service;

import com.unilife.model.dto.CommentRequestDto;
import com.unilife.model.dto.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto createComment(CommentRequestDto commentRequestDto, String username);
    List<CommentResponseDto> getCommentsByPostId(Long postId);
    List<CommentResponseDto> getRepliesToComment(Long parentCommentId); // For fetching replies
    CommentResponseDto updateComment(Long id, CommentRequestDto commentRequestDto, String username);
    void deleteComment(Long id, String username);
    CommentResponseDto getCommentById(Long id); // Added for completeness
}
