package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUserNo(Long userNo);
    Long countByUserNoAndNotificationIsCheck(Long userNo, boolean notificationIsCheck);
}
