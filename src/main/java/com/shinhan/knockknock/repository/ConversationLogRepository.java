package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.ConversationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationLogRepository extends JpaRepository<ConversationLogEntity, Long> {
}
