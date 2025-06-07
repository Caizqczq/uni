package com.unilife.service.impl;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.mapper.CourseMapper; // To fetch course details
import com.unilife.mapper.StudyGoalMapper;
import com.unilife.mapper.UserMapper;
import com.unilife.model.dto.StudyGoalDto;
import com.unilife.model.entity.Course; // Assuming Course entity is from schedule module
import com.unilife.model.entity.StudyGoal;
import com.unilife.model.entity.User;
import com.unilife.service.StudyPlanService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Arrays; // For status validation
import java.util.stream.Collectors;

// @Service
public class StudyPlanServiceImpl implements StudyPlanService {

    private final StudyGoalMapper studyGoalMapper;
    private final UserMapper userMapper;
    private final CourseMapper courseMapper; // Inject CourseMapper

    private static final List<String> VALID_STATUSES = Arrays.asList("PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED");

    // @Autowired
    public StudyPlanServiceImpl(StudyGoalMapper studyGoalMapper, UserMapper userMapper, CourseMapper courseMapper) {
        this.studyGoalMapper = studyGoalMapper;
        this.userMapper = userMapper;
        this.courseMapper = courseMapper;
    }

    @Override
    // @Transactional
    public StudyGoalDto createStudyGoal(StudyGoalDto goalDto, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }

        // Validate targetDate
        if (goalDto.getTargetDate() == null || goalDto.getTargetDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Target date must be today or in the future.");
        }

        // Validate courseId if present
        Course course = null;
        if (goalDto.getCourseId() != null) {
            course = courseMapper.findById(goalDto.getCourseId()); // Assuming courseId is for the Course entity from schedule
            if (course == null) {
                throw new ResourceNotFoundException("Course not found with id: " + goalDto.getCourseId());
            }
            // Optional: Check if the course belongs to the user (if courses are user-specific in schedule)
            // if (!Objects.equals(course.getUserId(), user.getId())) {
            //    throw new AccessDeniedException("User not authorized to create a goal for this course.");
            // }
        }

        StudyGoal goal = mapToEntity(goalDto);
        goal.setUserId(user.getId());
        goal.setStatus("PENDING"); // Default status
        goal.setCreatedAt(LocalDateTime.now());
        goal.setUpdatedAt(LocalDateTime.now());

        // Priority: use DTO's if set, otherwise entity default
        if (goalDto.getPriority() > 0) { // Assuming 0 is not a valid user-set priority
            goal.setPriority(goalDto.getPriority());
        }


        studyGoalMapper.save(goal);
        // Fetch DTO to include courseName
        return studyGoalMapper.findById(goal.getId());
    }

    @Override
    public StudyGoalDto getStudyGoalById(Long goalId, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        StudyGoalDto goalDto = studyGoalMapper.findById(goalId);
        if (goalDto == null) {
            throw new ResourceNotFoundException("StudyGoal not found with id: " + goalId);
        }
        if (!Objects.equals(goalDto.getUserId(), user.getId())) {
            // throw new AccessDeniedException("User not authorized to access this study goal");
            throw new RuntimeException("User not authorized to access this study goal (placeholder for AccessDeniedException)");
        }
        return goalDto;
    }

    @Override
    public List<StudyGoalDto> getUserStudyGoals(String username, String statusFilter, Long courseIdFilter) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }

        List<StudyGoalDto> goals;
        if (statusFilter != null && !statusFilter.isEmpty() && courseIdFilter != null) {
            // This combination might need a new mapper method or filtering in service layer
            // For now, let's filter by status first, then by courseId in service if needed, or assume mapper handles it
            // For simplicity, let's assume a direct mapper for status, and filter courseId in service if needed
            goals = studyGoalMapper.findByUserIdAndStatus(user.getId(), statusFilter);
            if (courseIdFilter != null) {
                goals = goals.stream().filter(g -> Objects.equals(g.getCourseId(), courseIdFilter)).collect(Collectors.toList());
            }
        } else if (statusFilter != null && !statusFilter.isEmpty()) {
            goals = studyGoalMapper.findByUserIdAndStatus(user.getId(), statusFilter);
        } else if (courseIdFilter != null) {
            goals = studyGoalMapper.findByUserIdAndCourseId(user.getId(), courseIdFilter);
        } else {
            goals = studyGoalMapper.findByUserId(user.getId());
        }
        return goals;
    }

    @Override
    // @Transactional
    public StudyGoalDto updateStudyGoal(Long goalId, StudyGoalDto goalDto, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        StudyGoal existingGoal = studyGoalMapper.findRawById(goalId); // Fetch raw entity
        if (existingGoal == null) {
            throw new ResourceNotFoundException("StudyGoal not found with id: " + goalId);
        }
        if (!Objects.equals(existingGoal.getUserId(), user.getId())) {
            // throw new AccessDeniedException("User not authorized to update this study goal");
            throw new RuntimeException("User not authorized to update this study goal (placeholder for AccessDeniedException)");
        }

        // Validate targetDate if being updated
        if (goalDto.getTargetDate() != null && goalDto.getTargetDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Target date must be today or in the future.");
        }
        // Validate status if being updated
        if (goalDto.getStatus() != null && !VALID_STATUSES.contains(goalDto.getStatus().toUpperCase())) {
            throw new IllegalArgumentException("Invalid status value. Valid statuses are: " + VALID_STATUSES);
        }
        // Validate courseId if being updated
        if (goalDto.getCourseId() != null && !Objects.equals(goalDto.getCourseId(), existingGoal.getCourseId())) {
            Course course = courseMapper.findById(goalDto.getCourseId());
            if (course == null) {
                throw new ResourceNotFoundException("Course not found with id: " + goalDto.getCourseId());
            }
            // Optional: Check course ownership if relevant
            // if (!Objects.equals(course.getUserId(), user.getId())) {
            //    throw new AccessDeniedException("User not authorized to associate this goal with this course.");
            // }
             existingGoal.setCourseId(goalDto.getCourseId());
        } else if (goalDto.getCourseId() == null && existingGoal.getCourseId() != null) {
            // Allow unsetting courseId
            existingGoal.setCourseId(null);
        }


        updateEntityFromDto(existingGoal, goalDto);
        existingGoal.setUpdatedAt(LocalDateTime.now());

        studyGoalMapper.update(existingGoal);
        return studyGoalMapper.findById(goalId); // Fetch DTO to include courseName
    }

    @Override
    // @Transactional
    public void deleteStudyGoal(Long goalId, String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        StudyGoal goal = studyGoalMapper.findRawById(goalId);
        if (goal == null) {
            // Optional: throw ResourceNotFoundException, or allow silent fail
            return; // Or throw new ResourceNotFoundException("StudyGoal not found with id: " + goalId);
        }
        if (!Objects.equals(goal.getUserId(), user.getId())) {
            // throw new AccessDeniedException("User not authorized to delete this study goal");
            throw new RuntimeException("User not authorized to delete this study goal (placeholder for AccessDeniedException)");
        }
        // TODO: If StudyTask is implemented, handle deletion of associated tasks here
        studyGoalMapper.deleteByIdAndUserId(goalId, user.getId());
    }

    // Helper methods for mapping
    private StudyGoalDto mapToDto(StudyGoal goal, String courseName) { // Used if courseName fetched separately
        if (goal == null) return null;
        StudyGoalDto dto = new StudyGoalDto();
        dto.setId(goal.getId());
        dto.setUserId(goal.getUserId());
        dto.setCourseId(goal.getCourseId());
        dto.setCourseName(courseName); // Set explicitly
        dto.setGoalTitle(goal.getGoalTitle());
        dto.setGoalDescription(goal.getGoalDescription());
        dto.setTargetDate(goal.getTargetDate());
        dto.setStatus(goal.getStatus());
        dto.setPriority(goal.getPriority());
        dto.setCreatedAt(goal.getCreatedAt());
        dto.setUpdatedAt(goal.getUpdatedAt());
        return dto;
    }

    private StudyGoal mapToEntity(StudyGoalDto dto) {
        if (dto == null) return null;
        StudyGoal goal = new StudyGoal();
        // ID is not set from DTO for creation
        goal.setCourseId(dto.getCourseId()); // May be null
        goal.setGoalTitle(dto.getGoalTitle());
        goal.setGoalDescription(dto.getGoalDescription());
        goal.setTargetDate(dto.getTargetDate());
        if (dto.getStatus() != null) { // Status from DTO if provided, otherwise default in entity
             if (!VALID_STATUSES.contains(dto.getStatus().toUpperCase())) {
                throw new IllegalArgumentException("Invalid status value. Valid statuses are: " + VALID_STATUSES);
            }
            goal.setStatus(dto.getStatus().toUpperCase());
        }
        if (dto.getPriority() > 0) { // Assuming 0 or less is not a valid user-set priority
             goal.setPriority(dto.getPriority());
        }
        // userId, createdAt, updatedAt are set in the service method or by entity defaults
        return goal;
    }

    private void updateEntityFromDto(StudyGoal goal, StudyGoalDto dto) {
        // courseId is handled in the main update method due to validation
        if (dto.getGoalTitle() != null) goal.setGoalTitle(dto.getGoalTitle());
        if (dto.getGoalDescription() != null) goal.setGoalDescription(dto.getGoalDescription());
        if (dto.getTargetDate() != null) goal.setTargetDate(dto.getTargetDate());
        if (dto.getStatus() != null) goal.setStatus(dto.getStatus().toUpperCase());
        if (dto.getPriority() > 0) goal.setPriority(dto.getPriority());
        // userId, createdAt should not be updated from DTO
        // updatedAt is handled by onUpdate or service method
    }
}
