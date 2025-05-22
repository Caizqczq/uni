package com.unilife.mapper;

import com.unilife.model.entity.User;
import com.unilife.model.entity.User;
import com.unilife.model.entity.VerificationToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface VerificationTokenMapper {
    void save(VerificationToken token);
    VerificationToken findByToken(@Param("token") String token);
    void deleteByToken(@Param("token") String token);
    void deleteByUserId(@Param("userId") Long userId);
    List<VerificationToken> findAllByUserId(@Param("userId") Long userId); // New method
    void updateTokenExpiry(@Param("token") String token, @Param("expiryDate") LocalDateTime expiryDate); // New method
}
