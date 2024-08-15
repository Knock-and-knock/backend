package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.CardHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardHistoryRepository extends JpaRepository<CardHistoryEntity, Long> {
}
