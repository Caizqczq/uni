package com.unilife.mapper;

import com.unilife.model.entity.SharedFile;
import com.unilife.model.dto.SharedFileResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SharedFileMapper {
    int save(SharedFile sharedFile);
    SharedFileResponseDto findById(@Param("id") Long id);
    SharedFile findRawById(@Param("id") Long id); // For fetching entity for deletion/update
    List<SharedFileResponseDto> findByCourseId(Map<String, Object> params); // For listing files related to a course with pagination
    long countByCourseId(@Param("courseId") Long courseId); // For pagination
    List<SharedFileResponseDto> findAll(Map<String, Object> params); // For listing all files with pagination
    long countAll(Map<String, Object> params); // For pagination
    int deleteById(@Param("id") Long id);
    List<SharedFileResponseDto> searchSharedFiles(Map<String, Object> params);
    long countSearchSharedFiles(Map<String, Object> params);
}
