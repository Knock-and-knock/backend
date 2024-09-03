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

    @Query("SELECT c FROM CardEntity c WHERE c.userNo = :userNo")
    List<CardEntity> findCardByUserNo(@Param("userNo") Long userNo);

    @Query("SELECT c.cardId FROM CardEntity c")
    List<Long> findAllCardIds();

    // 보호자 번호와 가족 카드 여부로 카드 조회
    @Query("SELECT c FROM CardEntity c WHERE c.userNo = :userNo AND c.cardIsfamily = true")
    List<CardEntity> findFamilyCardsByUserNo(@Param("userNo") Long userNo);

    // 이름과 전화번호로 CardEntity 조회
    @Query("SELECT c FROM CardEntity c WHERE c.cardUserKname = :cardUserKname AND c.cardUserPhone = :cardUserPhone")
    List<CardEntity> findByCardUserKnameAndCardUserPhone(@Param("cardUserKname") String cardUserKname, @Param("cardUserPhone") String cardUserPhone);

    @Query("SELECT c.userNo FROM CardEntity c WHERE c.cardId = :cardId")
    Long findUserNoByCardId(@Param("cardId") Long cardId);

    Long countByUserNo(Long userNo);

    @Query("SELECT COUNT(c) FROM CardEntity c WHERE c.cardUserKname = :userName AND c.cardUserPhone = :userPhone")
    Long countByUserNameAndUserPhone(@Param("userName") String userName, @Param("userPhone") String userPhone);

}
