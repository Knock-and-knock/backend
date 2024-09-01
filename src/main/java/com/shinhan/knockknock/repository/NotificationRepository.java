package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.NotificationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUserNoOrderByNotificationDateTimeDesc(Long userNo);
    Long countByUserNoAndNotificationIsCheck(Long userNo, boolean notificationIsCheck);
    @Transactional
    @Modifying

    // 알림 전체 읽음처리
    @Query("UPDATE NotificationEntity n SET n.notificationIsCheck = true WHERE n.userNo = :userNo")
    int readAllCheckedByUserNo(Long userNo);

    // 알림 상세 조회시 읽음처리
    @Transactional
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.notificationIsCheck = true WHERE n.notificationNo = :notificationNo")
    int readCheckedByNotificationNo(Long notificationNo);
}
