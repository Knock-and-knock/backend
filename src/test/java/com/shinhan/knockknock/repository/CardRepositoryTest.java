package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.CardEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    // 데이터베이스 테스트 전 초기화
    @BeforeEach
    public void setUp() {
        cardRepository.deleteAll();
    }

    @Test
    @Rollback
    public void FindByUserNoTest() {
        /*
        given
         */
        Long userNo = 1L;

        CardEntity cardEntity1 = new CardEntity();
        cardEntity1.setCardAccount("123456789");
        cardEntity1.setCardAddress("서울시 강남구");
        cardEntity1.setCardAmountDate(Date.valueOf("2024-12-01"));
        cardEntity1.setCardBank("Shinhan");
        cardEntity1.setCardCvc("123");
        cardEntity1.setCardEname("Hong Gil-dong");
        cardEntity1.setCardExpiredate(Date.valueOf("2029-12-01"));
        cardEntity1.setCardIsfamily(false);
        cardEntity1.setCardIssueNo(1L);
        cardEntity1.setCardNo("1111-2222-3333-4444");
        cardEntity1.setCardPassword("1234");
        cardEntity1.setUserNo(userNo);
        cardRepository.save(cardEntity1);

        CardEntity cardEntity2 = new CardEntity();
        cardEntity2.setCardAccount("987654321");
        cardEntity2.setCardAddress("서울시 서초구");
        cardEntity2.setCardAmountDate(Date.valueOf("2024-11-01"));
        cardEntity2.setCardBank("Kookmin");
        cardEntity2.setCardCvc("321");
        cardEntity2.setCardEname("Kim Yuna");
        cardEntity2.setCardExpiredate(Date.valueOf("2029-11-01"));
        cardEntity2.setCardIsfamily(false);
        cardEntity2.setCardIssueNo(2L);
        cardEntity2.setCardNo("5555-6666-7777-8888");
        cardEntity2.setCardPassword("5678");
        cardEntity2.setUserNo(userNo);
        cardRepository.save(cardEntity2);

        /*
         when
         */
        List<CardEntity> cards = cardRepository.findByUserNo(userNo);

        /*
         then
         */
        assertEquals(2, cards.size());
    }
}
