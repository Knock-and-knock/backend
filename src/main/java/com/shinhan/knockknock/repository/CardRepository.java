package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
    List<CardEntity> findByUserNo(Long userNo);

    @Query("SELECT c.cardNo FROM CardEntity c WHERE c.userNo = :userNo")
    List<String> findCardNoByUserNo(@Param("userNo") Long userNo);

}
