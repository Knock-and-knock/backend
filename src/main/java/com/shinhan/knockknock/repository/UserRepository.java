package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserId(String userId);
    UserEntity findByUserPhone(String userPhone);
    Optional<UserEntity> findByUserNameAndUserPhone(String userName, String userPhone);
    Boolean existsByUserId(String userId);
    Optional<UserEntity> findByCards_CardBankAndCards_CardAccountAndCards_CardIsfamilyFalse(String cardBank, String cardAccount);
    UserEntity findByUserNo(Long userNo);
}
