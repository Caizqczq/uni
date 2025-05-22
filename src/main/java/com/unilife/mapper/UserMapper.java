package com.unilife.mapper;

import com.unilife.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);
    User findByEmail(@Param("email") String email);
    boolean existsByUsername(@Param("username") String username);
    boolean existsByEmail(@Param("email") String email);
    void save(User user);
    void update(User user);
    User findById(@Param("id") Long id); // Added for completeness, might be useful
}
