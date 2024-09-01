package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.CardCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardCategoryRepository extends JpaRepository<CardCategoryEntity, Long> {

    @Query("SELECT c.cardCategoryName FROM CardCategoryEntity c WHERE c.cardCategoryNo = :cardCategoryNo")
    String findCardCategoryNameByCardCategoryNo(@Param("cardCategoryNo") Long cardCategoryNo);
}
