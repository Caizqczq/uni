package com.unilife.mapper;

import com.unilife.model.entity.Comment;
import com.unilife.model.dto.CommentResponseDto; // For methods returning joined results
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    int save(Comment comment);
    Comment findById(@Param("id") Long id); // Raw Comment entity
    CommentResponseDto findByIdWithDetails(@Param("id") Long id); // Comment with User details

    List<CommentResponseDto> findByPostIdWithDetails(@Param("postId") Long postId);
    List<CommentResponseDto> findByParentCommentIdWithDetails(@Param("parentCommentId") Long parentCommentId); // For replies

    int update(Comment comment);
    int deleteById(@Param("id") Long id);
    int deleteByPostId(@Param("postId") Long postId); // For cascading delete when a post is deleted
    int deleteByUserId(@Param("userId") Long userId); // For cascading delete when a user is deleted (optional)
}
