package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserId(String userId);
    UserEntity findByUserPhone(String phone);
}
