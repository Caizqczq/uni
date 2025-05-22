package com.unilife.mapper;

import com.unilife.model.dto.StudyGoalDto;
import com.unilife.model.entity.StudyGoal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudyGoalMapper {
    int save(StudyGoal goal);
    StudyGoalDto findById(@Param("id") Long id); // Returns DTO with courseName
    List<StudyGoalDto> findByUserId(@Param("userId") Long userId); // Returns DTOs with courseName
    List<StudyGoalDto> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status); // DTOs
    List<StudyGoalDto> findByUserIdAndCourseId(@Param("userId") Long userId, @Param("courseId") Long courseId); // DTOs
    int update(StudyGoal goal);
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
    StudyGoal findRawById(@Param("id") Long id); // To fetch the entity for updates
    List<StudyGoal> findPendingReminders(@Param("currentTime") LocalDateTime currentTime, @Param("userId") Long userId);
}
