package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
}
