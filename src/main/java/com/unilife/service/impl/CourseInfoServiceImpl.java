package com.unilife.service.impl;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.exception.UserAlreadyExistsException; // Placeholder for a more specific CourseAlreadyExistsException
import com.unilife.mapper.CourseInfoMapper;
import com.unilife.mapper.SharedDocumentMapper;
import com.unilife.model.dto.CourseInfoDto;
import com.unilife.model.entity.CourseInfo;
import com.unilife.service.CourseInfoService;
import com.unilife.utils.PageResponse; // Added for PageResponse
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap; // Added for params map
import java.util.List;
import java.util.Map; // Added for params map
import java.util.stream.Collectors;

// @Service
public class CourseInfoServiceImpl implements CourseInfoService {

    private final CourseInfoMapper courseInfoMapper;
    private final SharedDocumentMapper sharedDocumentMapper; // To check for linked documents before deletion

    // @Autowired
    public CourseInfoServiceImpl(CourseInfoMapper courseInfoMapper, SharedDocumentMapper sharedDocumentMapper) {
        this.courseInfoMapper = courseInfoMapper;
        this.sharedDocumentMapper = sharedDocumentMapper;
    }

    public PageResponse<CourseInfoDto> searchCourses(String searchTerm, int page, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchTerm", searchTerm);
        params.put("offset", page * size);
        params.put("limit", size);

        List<CourseInfo> courseInfos = courseInfoMapper.searchCourses(params);
        long totalElements = courseInfoMapper.countSearchCourses(params);

        List<CourseInfoDto> dtos = courseInfos.stream().map(this::mapToDto).collect(Collectors.toList());
        return new PageResponse<>(dtos, page, size, totalElements);
    }


    @Override
    // @Transactional
    public CourseInfoDto createCourseInfo(CourseInfoDto courseInfoDto) {
        if (courseInfoMapper.findByCourseCode(courseInfoDto.getCourseCode()) != null) {
            throw new UserAlreadyExistsException("Course with code '" + courseInfoDto.getCourseCode() + "' already exists.");
        }
        CourseInfo courseInfo = mapToEntity(courseInfoDto);
        courseInfoMapper.save(courseInfo);
        return mapToDto(courseInfo);
    }

    @Override
    public CourseInfoDto getCourseInfoById(Long id) {
        CourseInfo courseInfo = courseInfoMapper.findById(id);
        if (courseInfo == null) {
            throw new ResourceNotFoundException("CourseInfo not found with id: " + id);
        }
        return mapToDto(courseInfo);
    }

    @Override
    public CourseInfoDto getCourseInfoByCode(String courseCode) {
        CourseInfo courseInfo = courseInfoMapper.findByCourseCode(courseCode);
        if (courseInfo == null) {
            throw new ResourceNotFoundException("CourseInfo not found with code: " + courseCode);
        }
        return mapToDto(courseInfo);
    }

    @Override
    public List<CourseInfoDto> getAllCourseInfos() {
        List<CourseInfo> courseInfos = courseInfoMapper.findAll();
        return courseInfos.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    // @Transactional
    public CourseInfoDto updateCourseInfo(Long id, CourseInfoDto courseInfoDto) {
        CourseInfo existingCourseInfo = courseInfoMapper.findById(id);
        if (existingCourseInfo == null) {
            throw new ResourceNotFoundException("CourseInfo not found with id: " + id);
        }

        // Check for course code conflict if it's being changed
        if (courseInfoDto.getCourseCode() != null && !courseInfoDto.getCourseCode().equals(existingCourseInfo.getCourseCode())) {
            if (courseInfoMapper.findByCourseCode(courseInfoDto.getCourseCode()) != null) {
                throw new UserAlreadyExistsException("Course with code '" + courseInfoDto.getCourseCode() + "' already exists.");
            }
            existingCourseInfo.setCourseCode(courseInfoDto.getCourseCode());
        }

        if (courseInfoDto.getCourseName() != null) {
            existingCourseInfo.setCourseName(courseInfoDto.getCourseName());
        }
        if (courseInfoDto.getDescription() != null) {
            existingCourseInfo.setDescription(courseInfoDto.getDescription());
        }

        courseInfoMapper.update(existingCourseInfo);
        return mapToDto(existingCourseInfo);
    }

    @Override
    // @Transactional
    public void deleteCourseInfo(Long id) {
        CourseInfo existingCourseInfo = courseInfoMapper.findById(id);
        if (existingCourseInfo == null) {
            throw new ResourceNotFoundException("CourseInfo not found with id: " + id);
        }

        // Check if any shared documents are linked to this course
        long linkedDocumentsCount = sharedDocumentMapper.countByCourseId(id);
        if (linkedDocumentsCount > 0) {
            throw new IllegalStateException("Cannot delete course with id " + id + " as it has " +
                                            linkedDocumentsCount + " linked shared document(s). Please delete or reassign them first.");
        }

        courseInfoMapper.deleteById(id);
    }

    private CourseInfoDto mapToDto(CourseInfo entity) {
        if (entity == null) return null;
        return new CourseInfoDto(entity.getId(), entity.getCourseCode(), entity.getCourseName(), entity.getDescription());
    }

    private CourseInfo mapToEntity(CourseInfoDto dto) {
        if (dto == null) return null;
        CourseInfo entity = new CourseInfo();
        // ID is not set from DTO for creation
        entity.setCourseCode(dto.getCourseCode());
        entity.setCourseName(dto.getCourseName());
        entity.setDescription(dto.getDescription());
        // createdAt is set by entity constructor
        return entity;
    }
}
