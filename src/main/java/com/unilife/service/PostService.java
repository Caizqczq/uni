package com.unilife.service;

import com.unilife.model.dto.PostRequestDto;
import com.unilife.model.dto.PostResponseDto;
// import org.springframework.data.domain.Page; // Using a custom Page-like structure if not using Spring Data
import com.unilife.utils.PageResponse; // Custom PageResponse

public interface PostService {
    PostResponseDto createPost(PostRequestDto postRequestDto, String username);
    PostResponseDto getPostById(Long id);
    PageResponse<PostResponseDto> getAllPosts(int page, int size, Long topicId, Long userId);
    PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, String username);
    void deletePost(Long id, String username);
    void likePost(Long postId, String username);
    void unlikePost(Long postId, String username);
    PageResponse<PostResponseDto> searchPosts(String searchTerm, Long topicId, Long userId, int page, int size); // New method
}
