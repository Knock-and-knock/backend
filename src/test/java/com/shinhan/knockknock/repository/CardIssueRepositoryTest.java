package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.CardIssueEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class CardIssueRepositoryTest {

    @Autowired
    private CardIssueRepository cardIssueRepository;

    @Test
    @Rollback
    public void testSaveAndFindById() {
        // given
        CardIssueEntity cardIssueEntity = new CardIssueEntity();
        cardIssueEntity.setCardIssueResidentNo("987654-1234567");
        cardIssueEntity.setCardIssueEname("ASYNC SEUNGGEON");
        cardIssueEntity.setCardIssueEmail("guny1117@nate.com");
        cardIssueEntity.setCardIssueBank("카카오뱅크");
        cardIssueEntity.setCardIssueAccount("1231321312-231");
        cardIssueEntity.setCardIssueIsAgree(true);
        cardIssueEntity.setCardIssueIncome(500);
        cardIssueEntity.setCardIssueCredit(500);
        cardIssueEntity.setCardIssueAmountDate(Date.valueOf("2024-08-12"));
        cardIssueEntity.setCardIssueSource("월급");
        cardIssueEntity.setCardIssueIsHighrisk(true);
        cardIssueEntity.setCardIssuePurpose("생활비");
        cardIssueEntity.setUserNo(1L);
        cardIssueEntity.setCardIssueIsFamily(false);
        cardIssueEntity.setCardIssueAddress("서울시 관악구");

        // when
        CardIssueEntity savedEntity = cardIssueRepository.save(cardIssueEntity);
        Long generatedId = savedEntity.getCardIssueNo(); // 저장된 엔티티에서 ID를 가져옴
        Optional<CardIssueEntity> foundEntity = cardIssueRepository.findById(generatedId);

        // then
        assertTrue(foundEntity.isPresent());
        assertEquals("987654-1234567", foundEntity.get().getCardIssueResidentNo());
        assertEquals("ASYNC SEUNGGEON", foundEntity.get().getCardIssueEname());
        assertEquals("guny1117@nate.com", foundEntity.get().getCardIssueEmail());
    }
}
