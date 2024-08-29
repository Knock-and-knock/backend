package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.CardHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

public interface ConsumptionRepository extends JpaRepository<CardHistoryEntity, Long> {
    // 카드 번호 목록과 날짜 범위로 카드 이력 조회
    List<CardHistoryEntity> findCardHistoriesByCard_CardNoInAndCardHistoryApproveBetween(
            List<String> cardNo,
            Date startDate,
            Date endDate
    );

    // cardId와 현재 월을 기준으로 총 소비 금액 계산 (ReadCardSingleResponse 용도)
    @Query("SELECT SUM(c.cardHistoryAmount) FROM CardHistoryEntity c WHERE c.card.cardId = :cardId " +
            "AND c.cardHistoryApprove BETWEEN :startDate AND :endDate")
    Long findTotalAmountByCardIdAndCurrentMonth(@Param("cardId") Long cardId,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    // cardId와 날짜 범위로 카드 이력 조회 (소비리포트 사용)
    @Query("SELECT c FROM CardHistoryEntity c WHERE c.card.cardId = :cardId " +
            "AND c.cardHistoryApprove BETWEEN :startDate AND :endDate")
    List<CardHistoryEntity> findCardHistoriesByCardIdAndDateRange(
            @Param("cardId") Long cardId,
            @Param("startDate") java.sql.Date startDate,
            @Param("endDate") java.sql.Date endDate);


}



