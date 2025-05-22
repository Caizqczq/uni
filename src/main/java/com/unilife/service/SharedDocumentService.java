package com.unilife.service;

import com.unilife.model.dto.SharedDocumentRequestDto;
import com.unilife.model.dto.SharedDocumentResponseDto;
import com.unilife.utils.PageResponse; // If pagination is needed for getSharedDocumentsByCourseId

import java.util.List;

public interface SharedDocumentService {
    SharedDocumentResponseDto createSharedDocument(SharedDocumentRequestDto docDto, String username);
    SharedDocumentResponseDto getSharedDocumentById(Long id);
    // Returning List for now, can be PageResponse if pagination is needed for many docs per course
    List<SharedDocumentResponseDto> getSharedDocumentsByCourseId(Long courseId, int page, int size);
    SharedDocumentResponseDto updateSharedDocument(Long id, SharedDocumentRequestDto docDto, String username);
    void deleteSharedDocument(Long id, String username);
}
