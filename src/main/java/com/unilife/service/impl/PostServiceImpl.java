package com.unilife.service.impl;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.exception.UserAlreadyExistsException; // Placeholder for specific exceptions like AlreadyLikedException
import com.unilife.mapper.*;
import com.unilife.model.dto.PostRequestDto;
import com.unilife.model.dto.PostResponseDto;
import com.unilife.model.entity.Post;
import com.unilife.model.entity.PostLike;
import com.unilife.model.entity.Topic;
import com.unilife.model.entity.User;
import com.unilife.service.PostService;
import com.unilife.utils.PageResponse;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// @Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final TopicMapper topicMapper;
    private final PostLikeMapper postLikeMapper;
    private final CommentMapper commentMapper; // For deleting comments when a post is deleted

    // @Autowired
    public PostServiceImpl(PostMapper postMapper, UserMapper userMapper, TopicMapper topicMapper,
                           PostLikeMapper postLikeMapper, CommentMapper commentMapper) {
        this.postMapper = postMapper;
        this.userMapper = userMapper;
        this.topicMapper = topicMapper;
        this.postLikeMapper = postLikeMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    // @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        Topic topic = topicMapper.findById(postRequestDto.getTopicId());
        if (topic == null) {
            throw new ResourceNotFoundException("Topic not found with id: " + postRequestDto.getTopicId());
        }

        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setUserId(user.getId());
        post.setTopicId(topic.getId());
        // createdAt and updatedAt are set by default in Post entity constructor/onUpdate
        post.setLikesCount(0);

        postMapper.save(post);
        // The ID is now populated in 'post' object by MyBatis useGeneratedKeys

        // Return PostResponseDto by fetching the newly created post with details
        return postMapper.findByIdWithDetails(post.getId());
    }

    @Override
    public PostResponseDto getPostById(Long id) {
        PostResponseDto postResponseDto = postMapper.findByIdWithDetails(id);
        if (postResponseDto == null) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }
        return postResponseDto;
    }

    @Override
    public PageResponse<PostResponseDto> getAllPosts(int page, int size, Long topicId, Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("limit", size);
        if (topicId != null) {
            params.put("topicId", topicId);
        }
        if (userId != null) {
            params.put("userId", userId);
        }

        List<PostResponseDto> posts = postMapper.findAllWithDetails(params);
        long totalElements = postMapper.countAll(params);

        return new PageResponse<>(posts, page, size, totalElements);
    }

    @Override
    // @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, String username) {
        Post post = postMapper.findById(id);
        if (post == null) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }

        User currentUser = userMapper.findByUsername(username);
        if (currentUser == null || !Objects.equals(post.getUserId(), currentUser.getId())) {
            // throw new AccessDeniedException("User not authorized to update this post");
             throw new RuntimeException("User not authorized to update this post (placeholder for AccessDeniedException)");
        }

        if (postRequestDto.getTitle() != null) {
            post.setTitle(postRequestDto.getTitle());
        }
        if (postRequestDto.getContent() != null) {
            post.setContent(postRequestDto.getContent());
        }
        if (postRequestDto.getTopicId() != null) {
            Topic topic = topicMapper.findById(postRequestDto.getTopicId());
            if (topic == null) {
                throw new ResourceNotFoundException("Topic not found with id: " + postRequestDto.getTopicId());
            }
            post.setTopicId(topic.getId());
        }
        post.setUpdatedAt(LocalDateTime.now()); // Manually set update time

        postMapper.update(post);
        return postMapper.findByIdWithDetails(id);
    }

    @Override
    // @Transactional
    public void deletePost(Long id, String username) {
        Post post = postMapper.findById(id);
        if (post == null) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }

        User currentUser = userMapper.findByUsername(username);
        // Assuming ADMIN role check might be needed, or some other way to get roles
        boolean isAdmin = currentUser != null && currentUser.getRoles() != null && currentUser.getRoles().contains("ADMIN");

        if (currentUser == null || (!Objects.equals(post.getUserId(), currentUser.getId()) && !isAdmin)) {
            // throw new AccessDeniedException("User not authorized to delete this post");
            throw new RuntimeException("User not authorized to delete this post (placeholder for AccessDeniedException)");
        }

        // Delete associated likes and comments first
        postLikeMapper.deleteByPostId(id);
        commentMapper.deleteByPostId(id); // Assuming this method deletes all comments for a post
        postMapper.deleteById(id);
    }

    @Override
    // @Transactional
    public void likePost(Long postId, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new ResourceNotFoundException("Post not found: " + postId);
        }

        if (postLikeMapper.findByUserIdAndPostId(user.getId(), postId) != null) {
            throw new UserAlreadyExistsException("User has already liked this post."); // Or specific AlreadyLikedException
        }

        PostLike postLike = new PostLike(user, post);
        postLikeMapper.save(postLike);
        postMapper.incrementLikesCount(postId);
    }

    @Override
    // @Transactional
    public void unlikePost(Long postId, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new ResourceNotFoundException("Post not found: " + postId);
        }

        PostLike existingLike = postLikeMapper.findByUserIdAndPostId(user.getId(), postId);
        if (existingLike == null) {
            throw new ResourceNotFoundException("User has not liked this post.");
        }

        postLikeMapper.deleteByUserIdAndPostId(user.getId(), postId);
        postMapper.decrementLikesCount(postId);
    }
    
    // @Override // Ensure this is part of PostService interface if not already
    public PageResponse<PostResponseDto> searchPosts(String searchTerm, Long topicId, Long userId, int page, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchTerm", searchTerm);
        if (topicId != null) {
            params.put("topicId", topicId);
        }
        if (userId != null) {
            params.put("userId", userId); // This is authorId for the post
        }
        params.put("offset", page * size);
        params.put("limit", size);

        List<PostResponseDto> posts = postMapper.searchPosts(params);
        long totalElements = postMapper.countSearchPosts(params);

        return new PageResponse<>(posts, page, size, totalElements);
    }
}
