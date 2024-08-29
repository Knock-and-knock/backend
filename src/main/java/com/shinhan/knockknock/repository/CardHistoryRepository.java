package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.CardHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface CardHistoryRepository extends JpaRepository<CardHistoryEntity, Long> {
    List<CardHistoryEntity> findByCard_CardId(Long cardId);

    List<CardHistoryEntity> findByCard_CardIdAndCardHistoryApproveBetween(Long cardId, Timestamp startDate, Timestamp endDate);
}
