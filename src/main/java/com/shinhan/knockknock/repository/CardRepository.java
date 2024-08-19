package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
    List<CardEntity> findByUserNo(Long userNo);
}
