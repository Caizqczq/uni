package com.unilife.service.impl;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.mapper.SharedDocumentMapper;
import com.unilife.mapper.SharedFileMapper;
import com.unilife.mapper.UserMapper;
import com.unilife.model.dto.LearningMaterialSearchResultDto;
import com.unilife.model.dto.SharedDocumentResponseDto;
import com.unilife.model.dto.SharedFileResponseDto;
import com.unilife.model.entity.User;
import com.unilife.service.LearningMaterialSearchService;
import com.unilife.utils.PageResponse;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @Service
public class LearningMaterialSearchServiceImpl implements LearningMaterialSearchService {

    private final SharedDocumentMapper sharedDocumentMapper;
    private final SharedFileMapper sharedFileMapper;
    private final UserMapper userMapper;

    // @Autowired
    public LearningMaterialSearchServiceImpl(SharedDocumentMapper sharedDocumentMapper,
                                             SharedFileMapper sharedFileMapper,
                                             UserMapper userMapper) {
        this.sharedDocumentMapper = sharedDocumentMapper;
        this.sharedFileMapper = sharedFileMapper;
        this.userMapper = userMapper;
    }

    @Override
    public LearningMaterialSearchResultDto searchMaterials(String searchTerm, Long courseId,
                                                           int docPage, int docSize,
                                                           int filePage, int fileSize,
                                                           String username) {
        // Validate user
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        // Search Documents
        Map<String, Object> docParams = new HashMap<>();
        docParams.put("searchTerm", searchTerm);
        if (courseId != null) {
            docParams.put("courseId", courseId);
        }
        docParams.put("offset", docPage * docSize);
        docParams.put("limit", docSize);

        List<SharedDocumentResponseDto> documents = sharedDocumentMapper.searchSharedDocuments(docParams);
        long totalDocuments = sharedDocumentMapper.countSearchSharedDocuments(docParams);
        PageResponse<SharedDocumentResponseDto> documentPage = new PageResponse<>(documents, docPage, docSize, totalDocuments);

        // Search Files
        Map<String, Object> fileParams = new HashMap<>();
        fileParams.put("searchTerm", searchTerm);
        if (courseId != null) {
            fileParams.put("courseId", courseId);
        }
        fileParams.put("offset", filePage * fileSize);
        fileParams.put("limit", fileSize);

        List<SharedFileResponseDto> files = sharedFileMapper.searchSharedFiles(fileParams);
        long totalFiles = sharedFileMapper.countSearchSharedFiles(fileParams);
        PageResponse<SharedFileResponseDto> filePageResponse = new PageResponse<>(files, filePage, fileSize, totalFiles);

        return new LearningMaterialSearchResultDto(searchTerm, courseId, documentPage, filePageResponse);
    }
}
