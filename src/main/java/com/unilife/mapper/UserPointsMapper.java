package com.unilife.mapper;

import com.unilife.model.dto.UserPointsDto;
import com.unilife.model.entity.UserPoints;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserPointsMapper {
    UserPoints findByUserId(@Param("userId") Long userId);
    int save(UserPoints userPoints);
    int updatePoints(@Param("userId") Long userId, @Param("pointsDelta") int pointsDelta);
    List<UserPointsDto> findLeaderboard(@Param("limit") int limit);
}
