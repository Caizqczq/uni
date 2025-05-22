package com.unilife.service;

import com.unilife.model.dto.PointTransactionDto;
import com.unilife.model.dto.UserPointsDto;
import com.unilife.model.enums.PointActionType;
import com.unilife.utils.PageResponse;

import java.util.List;

public interface PointsService {
    void awardPoints(Long userId, PointActionType actionType, String description, Long relatedEntityId);
    UserPointsDto getUserPoints(String username);
    PageResponse<PointTransactionDto> getUserPointHistory(String username, int page, int size);
    List<UserPointsDto> getLeaderboard(int limit);
    void awardDailyLoginPoints(String username); // Specific method for daily login
}
