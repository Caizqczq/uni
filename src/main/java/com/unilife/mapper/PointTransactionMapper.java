package com.unilife.mapper;

import com.unilife.model.dto.PointTransactionDto;
import com.unilife.model.entity.PointTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map; // For pagination parameters

@Mapper
public interface PointTransactionMapper {
    int save(PointTransaction transaction);
    // findByUserId needs to join with users table to get username for PointTransactionDto
    List<PointTransactionDto> findByUserId(Map<String, Object> params);
    long countByUserId(@Param("userId") Long userId);
    // For daily login check
    PointTransaction findLastDailyLoginTransaction(@Param("userId") Long userId, @Param("actionType") String actionType);
}
