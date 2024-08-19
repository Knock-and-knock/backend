package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.CardIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardIssueRepository extends JpaRepository<CardIssueEntity, Long> {
    @Query("SELECT COUNT(c) FROM CardIssueEntity c WHERE c.userNo = :userNo")
    int countByUserNo(@Param("userNo") Long userNo);
}
