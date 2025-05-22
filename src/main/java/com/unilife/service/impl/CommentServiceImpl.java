package com.unilife.service.impl;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.mapper.CommentMapper;
import com.unilife.mapper.PostMapper;
import com.unilife.mapper.UserMapper;
import com.unilife.model.dto.CommentRequestDto;
import com.unilife.model.dto.CommentResponseDto;
import com.unilife.model.entity.Comment;
import com.unilife.model.entity.Post;
import com.unilife.model.entity.User;
import com.unilife.service.CommentService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

// @Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final UserMapper userMapper;
    private final PostMapper postMapper;

    // @Autowired
    public CommentServiceImpl(CommentMapper commentMapper, UserMapper userMapper, PostMapper postMapper) {
        this.commentMapper = commentMapper;
        this.userMapper = userMapper;
        this.postMapper = postMapper;
    }

    @Override
    // @Transactional
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        Post post = postMapper.findById(commentRequestDto.getPostId());
        if (post == null) {
            throw new ResourceNotFoundException("Post not found with id: " + commentRequestDto.getPostId());
        }

        Comment comment = new Comment();
        comment.setContent(commentRequestDto.getContent());
        comment.setUserId(user.getId());
        comment.setPostId(post.getId());

        if (commentRequestDto.getParentCommentId() != null) {
            Comment parentComment = commentMapper.findById(commentRequestDto.getParentCommentId());
            if (parentComment == null) {
                throw new ResourceNotFoundException("Parent comment not found with id: " + commentRequestDto.getParentCommentId());
            }
            // Ensure parent comment belongs to the same post
            if (!Objects.equals(parentComment.getPostId(), post.getId())) {
                throw new IllegalArgumentException("Parent comment does not belong to the specified post.");
            }
            comment.setParentCommentId(parentComment.getId());
        }

        commentMapper.save(comment);
        // The ID is now populated in 'comment' object by MyBatis useGeneratedKeys

        return commentMapper.findByIdWithDetails(comment.getId());
    }

    @Override
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        // Validate post exists
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }
        return commentMapper.findByPostIdWithDetails(postId);
    }
    
    @Override
    public CommentResponseDto getCommentById(Long id) {
        CommentResponseDto commentDto = commentMapper.findByIdWithDetails(id);
        if (commentDto == null) {
            throw new ResourceNotFoundException("Comment not found with id: " + id);
        }
        return commentDto;
    }


    @Override
    public List<CommentResponseDto> getRepliesToComment(Long parentCommentId) {
        // Validate parent comment exists
        Comment parentComment = commentMapper.findById(parentCommentId);
        if (parentComment == null) {
            throw new ResourceNotFoundException("Parent comment not found with id: " + parentCommentId);
        }
        return commentMapper.findByParentCommentIdWithDetails(parentCommentId);
    }

    @Override
    // @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto commentRequestDto, String username) {
        Comment comment = commentMapper.findById(id);
        if (comment == null) {
            throw new ResourceNotFoundException("Comment not found with id: " + id);
        }

        User currentUser = userMapper.findByUsername(username);
        if (currentUser == null || !Objects.equals(comment.getUserId(), currentUser.getId())) {
            // throw new AccessDeniedException("User not authorized to update this comment");
            throw new RuntimeException("User not authorized to update this comment (placeholder for AccessDeniedException)");
        }

        if (commentRequestDto.getContent() != null) {
            comment.setContent(commentRequestDto.getContent());
        }
        // Other fields like postId, parentCommentId are generally not updatable for a comment.

        commentMapper.update(comment);
        return commentMapper.findByIdWithDetails(id);
    }

    @Override
    // @Transactional
    public void deleteComment(Long id, String username) {
        Comment comment = commentMapper.findById(id);
        if (comment == null) {
            throw new ResourceNotFoundException("Comment not found with id: " + id);
        }

        User currentUser = userMapper.findByUsername(username);
        // Assuming ADMIN role check might be needed
        boolean isAdmin = currentUser != null && currentUser.getRoles() != null && currentUser.getRoles().contains("ADMIN");


        if (currentUser == null || (!Objects.equals(comment.getUserId(), currentUser.getId()) && !isAdmin)) {
            // throw new AccessDeniedException("User not authorized to delete this comment");
            throw new RuntimeException("User not authorized to delete this comment (placeholder for AccessDeniedException)");
        }

        // If the comment has replies, consider how to handle them:
        // Option 1: Disallow deletion.
        // Option 2: Set parent_comment_id of replies to null (promote them).
        // Option 3: Delete all replies (cascade).
        // For now, simple deletion. This needs business logic decision.
        // Example check (requires a method like commentMapper.countByParentCommentId(id)):
        // if (commentMapper.countByParentCommentId(id) > 0) {
        //    throw new IllegalStateException("Cannot delete comment with id " + id + " as it has replies.");
        // }
        commentMapper.deleteById(id);
    }
}
