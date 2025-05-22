package com.unilife.mapper;

import com.unilife.model.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
    int save(Course course);
    Course findById(@Param("id") Long id);
    List<Course> findByUserId(@Param("userId") Long userId);
    List<Course> findByUserIdAndDay(@Param("userId") Long userId, @Param("dayOfWeek") int dayOfWeek);
    int update(Course course);
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
    int deleteByUserId(@Param("userId") Long userId);
}
