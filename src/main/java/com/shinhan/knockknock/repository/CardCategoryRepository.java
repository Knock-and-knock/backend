package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.CardCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardCategoryRepository extends JpaRepository<CardCategoryEntity, Long> {
}
