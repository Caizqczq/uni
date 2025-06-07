package com.unilife.service.impl;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.mapper.PointTransactionMapper;
import com.unilife.mapper.UserMapper;
import com.unilife.mapper.UserPointsMapper;
import com.unilife.model.dto.PointTransactionDto;
import com.unilife.model.dto.UserPointsDto;
import com.unilife.model.entity.PointTransaction;
import com.unilife.model.entity.User;
import com.unilife.model.entity.UserPoints;
import com.unilife.model.enums.PointActionType;
import com.unilife.service.PointsService;
import com.unilife.utils.PageResponse;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @Service
public class PointsServiceImpl implements PointsService {

    private final UserPointsMapper userPointsMapper;
    private final PointTransactionMapper pointTransactionMapper;
    private final UserMapper userMapper;

    // @Autowired
    public PointsServiceImpl(UserPointsMapper userPointsMapper,
                             PointTransactionMapper pointTransactionMapper,
                             UserMapper userMapper) {
        this.userPointsMapper = userPointsMapper;
        this.pointTransactionMapper = pointTransactionMapper;
        this.userMapper = userMapper;
    }

    @Override
    // @Transactional
    public void awardPoints(Long userId, PointActionType actionType, String description, Long relatedEntityId) {
        User user = userMapper.findById(userId); // Validate user exists
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        int pointsToAward = actionType.getPoints();
        String finalDescription = (description != null && !description.isEmpty()) ? description : actionType.getDefaultDescription();

        // Record the transaction
        PointTransaction transaction = new PointTransaction(userId, pointsToAward, actionType, finalDescription, relatedEntityId);
        pointTransactionMapper.save(transaction);

        // Update or create user points
        UserPoints userPoints = userPointsMapper.findByUserId(userId);
        if (userPoints == null) {
            userPoints = new UserPoints(userId);
            userPoints.setTotalPoints(pointsToAward);
            userPointsMapper.save(userPoints);
        } else {
            userPointsMapper.updatePoints(userId, pointsToAward);
        }
    }

    @Override
    public UserPointsDto getUserPoints(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        UserPoints userPoints = userPointsMapper.findByUserId(user.getId());
        if (userPoints == null) {
            // Optionally create and return a zero-point record, or throw an exception/return null
            // For now, let's create it if it doesn't exist, as points can be awarded for various actions.
            UserPoints newUserPoints = new UserPoints(user.getId());
            userPointsMapper.save(newUserPoints);
            return new UserPointsDto(user.getId(), user.getUsername(), 0, newUserPoints.getLastUpdatedAt());

        }
        return new UserPointsDto(user.getId(), user.getUsername(), userPoints.getTotalPoints(), userPoints.getLastUpdatedAt());
    }

    @Override
    public PageResponse<PointTransactionDto> getUserPointHistory(String username, int page, int size) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getId());
        params.put("offset", page * size);
        params.put("limit", size);

        List<PointTransactionDto> transactions = pointTransactionMapper.findByUserId(params);
        long totalElements = pointTransactionMapper.countByUserId(user.getId());

        return new PageResponse<>(transactions, page, size, totalElements);
    }

    @Override
    public List<UserPointsDto> getLeaderboard(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive.");
        }
        return userPointsMapper.findLeaderboard(limit);
    }

    @Override
    // @Transactional
    public void awardDailyLoginPoints(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }

        // Check if daily login points already awarded today for this user
        PointTransaction lastLoginTransaction = pointTransactionMapper.findLastDailyLoginTransaction(user.getId(), PointActionType.DAILY_LOGIN.name());

        boolean shouldAward = true;
        if (lastLoginTransaction != null) {
            if (lastLoginTransaction.getTransactionDate().toLocalDate().isEqual(LocalDate.now())) {
                shouldAward = false; // Already awarded today
            }
        }

        if (shouldAward) {
            awardPoints(user.getId(), PointActionType.DAILY_LOGIN, null, null);
        } else {
            System.out.println("Daily login points already awarded today for user: " + username);
            // Optionally, could throw an exception or return a status if client needs to know
        }
    }
}
