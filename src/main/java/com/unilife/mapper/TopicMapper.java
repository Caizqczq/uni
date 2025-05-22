package com.unilife.mapper;

import com.unilife.model.entity.Topic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TopicMapper {
    int save(Topic topic); // Changed to int to reflect rows affected
    Topic findById(@Param("id") Long id);
    Topic findByName(@Param("name") String name);
    List<Topic> findAll();
    int update(Topic topic); // Changed to int
    int deleteById(@Param("id") Long id); // Changed to int
}
