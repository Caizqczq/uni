package com.unilife.mapper;

import com.unilife.model.entity.PersonalEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PersonalEventMapper {
    int save(PersonalEvent event);
    PersonalEvent findById(@Param("id") Long id);
    List<PersonalEvent> findByUserId(@Param("userId") Long userId);
    List<PersonalEvent> findByUserIdAndDateRange(@Param("userId") Long userId,
                                                 @Param("rangeStart") LocalDateTime rangeStart,
                                                 @Param("rangeEnd") LocalDateTime rangeEnd);
    int update(PersonalEvent event);
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
    List<PersonalEvent> findPendingReminders(@Param("currentTime") LocalDateTime currentTime, @Param("userId") Long userId);
}
