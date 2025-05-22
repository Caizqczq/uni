package com.unilife.service;

import com.unilife.model.dto.StudyGoalDto;
import java.util.List;

public interface StudyPlanService {
    StudyGoalDto createStudyGoal(StudyGoalDto goalDto, String username);
    StudyGoalDto getStudyGoalById(Long goalId, String username);
    List<StudyGoalDto> getUserStudyGoals(String username, String statusFilter, Long courseIdFilter);
    StudyGoalDto updateStudyGoal(Long goalId, StudyGoalDto goalDto, String username);
    void deleteStudyGoal(Long goalId, String username);
}
