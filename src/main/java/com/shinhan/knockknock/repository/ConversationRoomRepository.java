package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.ConversationRoomEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface ConversationRoomRepository extends JpaRepository<ConversationRoomEntity, Long> {

    @Query("SELECT c FROM ConversationRoomEntity c WHERE c.user.userNo = :userNo AND c.conversationRoomNo <> :conversationRoomNo ORDER BY c.conversationRoomStartAt DESC")
    List<ConversationRoomEntity> findTopByUserNoExcludingConversationRoomNo(@Param("userNo") long userNo, @Param("conversationRoomNo") long conversationRoomNo, Pageable pageable);

    default ConversationRoomEntity findLatestByUserNoExcludingConversationRoomNo(long userNo, long conversationRoomNo) {
        Pageable pageable = PageRequest.of(0, 1);  // 첫 번째 페이지에서 하나의 결과만
        List<ConversationRoomEntity> results = findTopByUserNoExcludingConversationRoomNo(userNo, conversationRoomNo, pageable);
        return results.isEmpty() ? null : results.get(0);
    }

    @Query("SELECT c FROM ConversationRoomEntity c WHERE c.user.userNo = :userNo ORDER BY c.conversationRoomEndAt DESC NULLS LAST")
    List<ConversationRoomEntity> findTopByUserUserNoOrderByConversationRoomEndAtDesc(@Param("userNo") long userNo, Pageable pageable);

    default ConversationRoomEntity findLatestByUserNo(long userNo) {
        Pageable pageable = PageRequest.of(0, 1);  // 첫 번째 페이지에서 하나의 결과만
        List<ConversationRoomEntity> results = findTopByUserUserNoOrderByConversationRoomEndAtDesc(userNo, pageable);
        return results.isEmpty() ? null : results.get(0);
    }

    @Query("SELECT cr FROM ConversationRoomEntity cr " +
            "WHERE cr.conversationRoomStartAt < :threeDaysAgo")
    List<ConversationRoomEntity> findRoomsInactiveSince(@Param("threeDaysAgo") Timestamp threeDaysAgo);

}
