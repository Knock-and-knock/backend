package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.domain.entity.CardHistoryEntity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CardHistoryRepository extends JpaRepository<CardHistoryEntity, Long> {
    List<CardHistoryEntity> findByCard_CardId(Long cardId);

    @Query(value = "SELECT c.card_id " +
            "FROM cardhistory_tb ch " +
            "JOIN card_tb c ON ch.card_id = c.card_id " +
            "WHERE ch.cardhistory_approve >= (CURRENT_DATE - INTERVAL '1 month') " +
            "AND c.user_no = :userId " +
            "GROUP BY c.card_id " +
            "ORDER BY COUNT(ch.cardhistory_no) DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<Long> findTopUsedCardNoLastMonthByUser(@Param("userId") Long userId);

    List<CardHistoryEntity> findByCard_CardId(Long cardId, PageRequest pageRequest);
    List<CardHistoryEntity> findByCard_CardId(Long cardId, Sort sort);
    List<CardHistoryEntity> findByCard_CardIdAndCardHistoryApproveBetween(Long cardId, Timestamp startDate, Timestamp endDate, Sort sort);

    // 2일 이내에 사용 내역이 없는 카드 조회
    @Query("SELECT c.cardId FROM CardEntity c " +
            "WHERE c.cardId NOT IN (" +
            "  SELECT ch.card.cardId FROM CardHistoryEntity ch " +
            "  WHERE ch.cardHistoryApprove >= :twoDaysAgo" +
            ")")
    List<Long> findCardIdsWithoutRecentUse(@Param("twoDaysAgo") Timestamp twoDaysAgo);

}
