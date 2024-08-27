package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.CardHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface ConsumptionRepository extends JpaRepository<CardHistoryEntity, Long> {
    // 카드 번호 목록과 날짜 범위로 카드 이력 조회
    List<CardHistoryEntity> findCardHistoriesByCard_CardNoInAndCardHistoryApproveBetween(
            List<String> cardNo,
            Date startDate,
            Date endDate
    );
}



