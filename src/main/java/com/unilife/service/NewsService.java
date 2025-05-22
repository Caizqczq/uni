package com.unilife.service;

import com.unilife.model.dto.NewsArticleDto;
import com.unilife.utils.PageResponse;

public interface NewsService {
    NewsArticleDto createNewsArticle(NewsArticleDto newsArticleDto, String username);
    NewsArticleDto getNewsArticleById(Long id);
    PageResponse<NewsArticleDto> getAllNewsArticles(int page, int size);
    NewsArticleDto updateNewsArticle(Long id, NewsArticleDto newsArticleDto, String username);
    void deleteNewsArticle(Long id, String username);
}
