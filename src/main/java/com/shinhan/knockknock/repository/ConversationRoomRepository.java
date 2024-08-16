package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.ConversationRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRoomRepository extends JpaRepository<ConversationRoomEntity, Long> {
}
