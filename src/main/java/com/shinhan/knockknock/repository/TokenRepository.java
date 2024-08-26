package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.TokenEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByRefreshToken(String refreshToken);
    Optional<TokenEntity> findByUser(UserEntity user);
    Optional<TokenEntity> findByUser_UserNo(long userNo);
}
