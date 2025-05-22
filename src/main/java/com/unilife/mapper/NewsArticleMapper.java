package com.unilife.mapper;

import com.unilife.model.entity.NewsArticle;
import com.unilife.model.dto.NewsArticleDto; // For methods returning joined results
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface NewsArticleMapper {
    int save(NewsArticle newsArticle);
    NewsArticleDto findById(@Param("id") Long id); // Should join with User to get postedBy
    List<NewsArticleDto> findAll(Map<String, Object> params); // Should join with User
    long countAll(Map<String, Object> params);
    int update(NewsArticle newsArticle);
    int deleteById(@Param("id") Long id);
    NewsArticle findRawById(@Param("id") Long id); // To get the raw entity for updates
}
