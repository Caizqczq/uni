package com.unilife.service;

import com.unilife.model.dto.CourseInfoDto;
import java.util.List;

public interface CourseInfoService {
    CourseInfoDto createCourseInfo(CourseInfoDto courseInfoDto);
    CourseInfoDto getCourseInfoById(Long id);
    CourseInfoDto getCourseInfoByCode(String courseCode);
    List<CourseInfoDto> getAllCourseInfos();
    CourseInfoDto updateCourseInfo(Long id, CourseInfoDto courseInfoDto); // Added for completeness
    void deleteCourseInfo(Long id); // Added for completeness
    PageResponse<CourseInfoDto> searchCourses(String searchTerm, int page, int size); // New method
}
