package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}
