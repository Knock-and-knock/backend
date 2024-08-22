package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.MatchEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    Optional<MatchEntity> findByUserProtectorAndUserProtege(UserEntity protector, UserEntity protege);
    Optional<MatchEntity> findByUserProtectorOrUserProtege(UserEntity protector, UserEntity protege);
}
