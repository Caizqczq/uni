package com.unilife.mapper;

import com.unilife.model.entity.SharedDocument;
import com.unilife.model.dto.SharedDocumentResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map; // For pagination parameters

@Mapper
public interface SharedDocumentMapper {
    int save(SharedDocument document);
    SharedDocumentResponseDto findById(@Param("id") Long id);
    List<SharedDocumentResponseDto> findByCourseId(Map<String, Object> params); // For pagination
    long countByCourseId(@Param("courseId") Long courseId); // For pagination
    int update(SharedDocument document);
    int deleteById(@Param("id") Long id);
    SharedDocument findRawById(@Param("id") Long id); // To get the raw entity for updates
    List<SharedDocumentResponseDto> searchSharedDocuments(Map<String, Object> params);
    long countSearchSharedDocuments(Map<String, Object> params);
}
