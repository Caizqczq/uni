package com.unilife.service.impl;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.mapper.CourseInfoMapper;
import com.unilife.mapper.SharedDocumentMapper;
import com.unilife.mapper.SharedFileMapper;
import com.unilife.mapper.UserMapper; // To validate user
import com.unilife.model.dto.CourseRelatedResourcesDto;
import com.unilife.model.dto.SharedDocumentResponseDto;
import com.unilife.model.dto.SharedFileResponseDto;
import com.unilife.model.entity.CourseInfo;
import com.unilife.model.entity.User;
import com.unilife.service.CourseIntelligenceService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @Service
public class CourseIntelligenceServiceImpl implements CourseIntelligenceService {

    private final CourseInfoMapper courseInfoMapper;
    private final SharedDocumentMapper sharedDocumentMapper;
    private final SharedFileMapper sharedFileMapper;
    private final UserMapper userMapper; // To ensure the user is valid

    // @Autowired
    public CourseIntelligenceServiceImpl(CourseInfoMapper courseInfoMapper,
                                         SharedDocumentMapper sharedDocumentMapper,
                                         SharedFileMapper sharedFileMapper,
                                         UserMapper userMapper) {
        this.courseInfoMapper = courseInfoMapper;
        this.sharedDocumentMapper = sharedDocumentMapper;
        this.sharedFileMapper = sharedFileMapper;
        this.userMapper = userMapper;
    }

    @Override
    public CourseRelatedResourcesDto getRelatedResourcesForCourse(Long courseId, String username) {
        // Validate user (optional, but good practice for authenticated endpoints)
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        // Fetch CourseInfo details
        CourseInfo courseInfo = courseInfoMapper.findById(courseId);
        if (courseInfo == null) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }

        // For fetching all, we can pass null for limit/offset or a very large limit
        // to the existing paginated mapper methods. Let's assume current mappers
        // will fetch all if limit/offset are not provided or are null.
        // Alternatively, create new mapper methods like `findAllByCourseId`.
        // For simplicity, using existing methods with high limit.
        Map<String, Object> params = new HashMap<>();
        params.put("courseId", courseId);
        // Not setting limit/offset to fetch all, assuming mapper handles this
        // or using a very large limit if mapper requires it for "all"
        // params.put("limit", Integer.MAX_VALUE);
        // params.put("offset", 0);


        List<SharedDocumentResponseDto> sharedDocuments = sharedDocumentMapper.findByCourseId(params);
        List<SharedFileResponseDto> sharedFiles = sharedFileMapper.findByCourseId(params); // Assuming same param structure

        return new CourseRelatedResourcesDto(
                courseInfo.getId(),
                courseInfo.getCourseName(), // Or courseInfo.getCourseCode() + " - " + courseInfo.getCourseName()
                sharedDocuments,
                sharedFiles
        );
    }
}
