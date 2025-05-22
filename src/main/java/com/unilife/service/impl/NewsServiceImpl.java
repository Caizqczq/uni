package com.unilife.service.impl;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.mapper.NewsArticleMapper;
import com.unilife.mapper.UserMapper;
import com.unilife.model.dto.NewsArticleDto;
import com.unilife.model.entity.NewsArticle;
import com.unilife.model.entity.User;
import com.unilife.service.NewsService;
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
public class NewsServiceImpl implements NewsService {

    private final NewsArticleMapper newsArticleMapper;
    private final UserMapper userMapper;

    // @Autowired
    public NewsServiceImpl(NewsArticleMapper newsArticleMapper, UserMapper userMapper) {
        this.newsArticleMapper = newsArticleMapper;
        this.userMapper = userMapper;
    }

    @Override
    // @Transactional
    public NewsArticleDto createNewsArticle(NewsArticleDto newsArticleDto, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }

        NewsArticle newsArticle = mapToEntity(newsArticleDto);
        newsArticle.setUserId(user.getId());
        // createdAt, updatedAt, publishedAt are handled by entity default or can be set from DTO
        if (newsArticleDto.getPublishedAt() == null) {
            newsArticle.setPublishedAt(newsArticle.getCreatedAt()); // Default if not provided
        }


        newsArticleMapper.save(newsArticle);
        // The ID is now populated in 'newsArticle' object by MyBatis useGeneratedKeys

        // Return DTO by fetching the newly created article with details (including postedBy)
        return newsArticleMapper.findById(newsArticle.getId());
    }

    @Override
    public NewsArticleDto getNewsArticleById(Long id) {
        NewsArticleDto newsArticleDto = newsArticleMapper.findById(id);
        if (newsArticleDto == null) {
            throw new ResourceNotFoundException("News article not found with id: " + id);
        }
        return newsArticleDto;
    }

    @Override
    public PageResponse<NewsArticleDto> getAllNewsArticles(int page, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("limit", size);

        List<NewsArticleDto> articles = newsArticleMapper.findAll(params);
        long totalElements = newsArticleMapper.countAll(params);

        return new PageResponse<>(articles, page, size, totalElements);
    }

    @Override
    // @Transactional
    public NewsArticleDto updateNewsArticle(Long id, NewsArticleDto newsArticleDto, String username) {
        NewsArticle newsArticle = newsArticleMapper.findRawById(id); // Fetch raw entity
        if (newsArticle == null) {
            throw new ResourceNotFoundException("News article not found with id: " + id);
        }

        User currentUser = userMapper.findByUsername(username);
        // Placeholder for permission check (admin or original creator)
        boolean isAdmin = currentUser != null && currentUser.getRoles() != null && currentUser.getRoles().contains("ADMIN");
        if (currentUser == null || (!Objects.equals(newsArticle.getUserId(), currentUser.getId()) && !isAdmin)) {
            // throw new AccessDeniedException("User not authorized to update this news article");
            throw new RuntimeException("User not authorized to update this news article (placeholder for AccessDeniedException)");
        }

        if (newsArticleDto.getTitle() != null) {
            newsArticle.setTitle(newsArticleDto.getTitle());
        }
        if (newsArticleDto.getContent() != null) {
            newsArticle.setContent(newsArticleDto.getContent());
        }
        if (newsArticleDto.getSource() != null) {
            newsArticle.setSource(newsArticleDto.getSource());
        }
        if (newsArticleDto.getAuthor() != null) {
            newsArticle.setAuthor(newsArticleDto.getAuthor());
        }
        if (newsArticleDto.getPublishedAt() != null) {
            newsArticle.setPublishedAt(newsArticleDto.getPublishedAt());
        }
        newsArticle.setUpdatedAt(LocalDateTime.now()); // Manually set update time
        newsArticle.setUserId(currentUser.getId()); // The user performing the update

        newsArticleMapper.update(newsArticle);
        return newsArticleMapper.findById(id); // Return DTO with potentially updated postedBy info
    }

    @Override
    // @Transactional
    public void deleteNewsArticle(Long id, String username) {
        NewsArticle newsArticle = newsArticleMapper.findRawById(id);
        if (newsArticle == null) {
            throw new ResourceNotFoundException("News article not found with id: " + id);
        }

        User currentUser = userMapper.findByUsername(username);
        // Placeholder for permission check (admin or original creator)
        boolean isAdmin = currentUser != null && currentUser.getRoles() != null && currentUser.getRoles().contains("ADMIN");
        if (currentUser == null || (!Objects.equals(newsArticle.getUserId(), currentUser.getId()) && !isAdmin )) {
            // throw new AccessDeniedException("User not authorized to delete this news article");
             throw new RuntimeException("User not authorized to delete this news article (placeholder for AccessDeniedException)");
        }

        newsArticleMapper.deleteById(id);
    }

    private NewsArticle mapToEntity(NewsArticleDto dto) {
        if (dto == null) return null;
        NewsArticle entity = new NewsArticle();
        entity.setId(dto.getId()); // Usually null for creation, set for update
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setSource(dto.getSource());
        entity.setAuthor(dto.getAuthor());
        entity.setPublishedAt(dto.getPublishedAt());
        // userId is set in service method
        // createdAt and updatedAt are typically handled by entity/DB, but can be passed if needed
        if (dto.getCreatedAt() != null) entity.setCreatedAt(dto.getCreatedAt());
        if (dto.getUpdatedAt() != null) entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    // mapToDto is essentially done by the mapper query that returns NewsArticleDto directly
}
