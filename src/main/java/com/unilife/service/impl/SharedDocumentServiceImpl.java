package com.unilife.service.impl;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.mapper.CourseInfoMapper;
import com.unilife.mapper.SharedDocumentMapper;
import com.unilife.mapper.UserMapper;
import com.unilife.model.dto.CourseInfoDto;
import com.unilife.model.dto.SharedDocumentRequestDto;
import com.unilife.model.dto.SharedDocumentResponseDto;
import com.unilife.model.entity.CourseInfo;
import com.unilife.model.entity.SharedDocument;
import com.unilife.model.entity.User;
import com.unilife.service.SharedDocumentService;
import com.unilife.utils.PageResponse; // For PageResponse
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
public class SharedDocumentServiceImpl implements SharedDocumentService {

    private final SharedDocumentMapper sharedDocumentMapper;
    private final UserMapper userMapper;
    private final CourseInfoMapper courseInfoMapper; // To validate courseId

    // @Autowired
    public SharedDocumentServiceImpl(SharedDocumentMapper sharedDocumentMapper, UserMapper userMapper, CourseInfoMapper courseInfoMapper) {
        this.sharedDocumentMapper = sharedDocumentMapper;
        this.userMapper = userMapper;
        this.courseInfoMapper = courseInfoMapper;
    }

    @Override
    // @Transactional
    public SharedDocumentResponseDto createSharedDocument(SharedDocumentRequestDto docDto, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        CourseInfo course = courseInfoMapper.findById(docDto.getCourseId());
        if (course == null) {
            throw new ResourceNotFoundException("CourseInfo not found with id: " + docDto.getCourseId());
        }

        SharedDocument document = new SharedDocument();
        document.setTitle(docDto.getTitle());
        document.setContent(docDto.getContent());
        document.setCourseId(course.getId());
        document.setCreatedByUserId(user.getId());
        document.setLastUpdatedByUserId(user.getId());
        document.setVersion(1); // Initial version
        // createdAt and updatedAt are set by entity default constructor

        sharedDocumentMapper.save(document);
        // The ID is now populated in 'document' object by MyBatis useGeneratedKeys

        return sharedDocumentMapper.findById(document.getId());
    }

    @Override
    public SharedDocumentResponseDto getSharedDocumentById(Long id) {
        SharedDocumentResponseDto docDto = sharedDocumentMapper.findById(id);
        if (docDto == null) {
            throw new ResourceNotFoundException("SharedDocument not found with id: " + id);
        }
        return docDto;
    }

    @Override
    public List<SharedDocumentResponseDto> getSharedDocumentsByCourseId(Long courseId, int page, int size) {
        // Validate course exists
        CourseInfo course = courseInfoMapper.findById(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("CourseInfo not found with id: " + courseId);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("courseId", courseId);
        params.put("offset", page * size);
        params.put("limit", size);
        // If you want to return PageResponse, you'd also call countByCourseId
        // long totalElements = sharedDocumentMapper.countByCourseId(courseId);
        // List<SharedDocumentResponseDto> documents = sharedDocumentMapper.findByCourseId(params);
        // return new PageResponse<>(documents, page, size, totalElements);
        return sharedDocumentMapper.findByCourseId(params);
    }


    @Override
    // @Transactional
    public SharedDocumentResponseDto updateSharedDocument(Long id, SharedDocumentRequestDto docDto, String username) {
        SharedDocument document = sharedDocumentMapper.findRawById(id); // Fetch raw entity
        if (document == null) {
            throw new ResourceNotFoundException("SharedDocument not found with id: " + id);
        }

        User currentUser = userMapper.findByUsername(username);
        if (currentUser == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }

        // Simple permission: any authenticated user can edit for now.
        // More complex: check if user is creator, or part of a course, or admin.
        // if (!Objects.equals(document.getCreatedByUserId(), currentUser.getId())) {
        //    throw new AccessDeniedException("User not authorized to update this document");
        // }

        if (docDto.getTitle() != null) {
            document.setTitle(docDto.getTitle());
        }
        if (docDto.getContent() != null) {
            document.setContent(docDto.getContent());
        }
        if (docDto.getCourseId() != null && !Objects.equals(docDto.getCourseId(), document.getCourseId())) {
             CourseInfo course = courseInfoMapper.findById(docDto.getCourseId());
            if (course == null) {
                throw new ResourceNotFoundException("CourseInfo not found with id: " + docDto.getCourseId());
            }
            document.setCourseId(course.getId());
        }

        document.setLastUpdatedByUserId(currentUser.getId());
        document.setUpdatedAt(LocalDateTime.now()); // Manually trigger update time
        document.setVersion(document.getVersion() + 1); // Increment version

        sharedDocumentMapper.update(document);
        return sharedDocumentMapper.findById(id); // Return DTO with potentially updated user/course info
    }

    @Override
    // @Transactional
    public void deleteSharedDocument(Long id, String username) {
        SharedDocument document = sharedDocumentMapper.findRawById(id);
        if (document == null) {
            throw new ResourceNotFoundException("SharedDocument not found with id: " + id);
        }

        User currentUser = userMapper.findByUsername(username);
        // Placeholder for permission check (admin or original creator)
        boolean isAdmin = currentUser != null && currentUser.getRoles() != null && currentUser.getRoles().contains("ADMIN");
        if (currentUser == null || (!Objects.equals(document.getCreatedByUserId(), currentUser.getId()) && !isAdmin )) {
            // throw new AccessDeniedException("User not authorized to delete this document");
            throw new RuntimeException("User not authorized to delete this document (placeholder for AccessDeniedException)");
        }

        sharedDocumentMapper.deleteById(id);
    }
}
