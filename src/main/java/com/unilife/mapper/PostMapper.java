package com.unilife.mapper;

import com.unilife.model.entity.Post;
import com.unilife.model.dto.PostResponseDto; // For methods returning joined results
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper {
    int save(Post post);
    Post findById(@Param("id") Long id); // Raw Post entity
    PostResponseDto findByIdWithDetails(@Param("id") Long id); // Post with User and Topic details

    List<PostResponseDto> findAllWithDetails(Map<String, Object> params);
    long countAll(Map<String, Object> params);

    int update(Post post);
    int deleteById(@Param("id") Long id);

    int incrementLikesCount(@Param("id") Long id);
    int decrementLikesCount(@Param("id") Long id);

    List<PostResponseDto> searchPosts(Map<String, Object> params);
    long countSearchPosts(Map<String, Object> params);
}
