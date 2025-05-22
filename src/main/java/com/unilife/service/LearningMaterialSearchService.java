package com.unilife.service;

import com.unilife.model.dto.LearningMaterialSearchResultDto;

public interface LearningMaterialSearchService {
    LearningMaterialSearchResultDto searchMaterials(String searchTerm, Long courseId,
                                                    int docPage, int docSize,
                                                    int filePage, int fileSize,
                                                    String username); // Added username for user validation
}
