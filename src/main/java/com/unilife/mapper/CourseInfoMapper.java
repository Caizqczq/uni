package com.unilife.mapper;

import com.unilife.model.entity.CourseInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseInfoMapper {
    int save(CourseInfo courseInfo);
    CourseInfo findById(@Param("id") Long id);
    CourseInfo findByCourseCode(@Param("courseCode") String courseCode);
    List<CourseInfo> findAll();
    int update(CourseInfo courseInfo);
    int deleteById(@Param("id") Long id);
    List<CourseInfo> searchCourses(Map<String, Object> params); // New method for search
    long countSearchCourses(Map<String, Object> params); // New method for counting search results
}
