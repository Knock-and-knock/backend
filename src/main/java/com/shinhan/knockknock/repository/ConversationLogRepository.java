package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.ConversationLogEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collections;
import java.util.List;

public interface ConversationLogRepository extends JpaRepository<ConversationLogEntity, Long> {

    default List<ConversationLogEntity> findLast5ByConversationRoomNo(long conversationRoomNo) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "conversationLogDatetime"));
        List<ConversationLogEntity> logs = findByConversationRoomNo(conversationRoomNo, pageable);
        Collections.reverse(logs);  // 결과를 역순으로 뒤집음
        return logs;
    }

    List<ConversationLogEntity> findByConversationRoomNo(long conversationRoomNo, Pageable pageable);

}
