package com.unilife.mapper;

import com.unilife.model.entity.PostLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostLikeMapper {
    int save(PostLike postLike);
    PostLike findByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);
    int deleteById(@Param("id") Long id); // Added for completeness, though deleteByUserIdAndPostId is more common
    int deleteByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);
    int deleteByPostId(@Param("postId") Long postId); // For cascading delete when a post is deleted
    int countByPostId(@Param("postId") Long postId);
}
