package com.unilife.service;

import com.unilife.model.dto.CourseRelatedResourcesDto;

public interface CourseIntelligenceService {
    CourseRelatedResourcesDto getRelatedResourcesForCourse(Long courseId, String username);
}
