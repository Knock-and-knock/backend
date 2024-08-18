package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.ConversationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationLogRepository extends JpaRepository<ConversationLogEntity, Long> {

    List<ConversationLogEntity> findByConversationRoomNoOrderByConversationLogDatetimeAsc(long conversationRoomNo);

}
