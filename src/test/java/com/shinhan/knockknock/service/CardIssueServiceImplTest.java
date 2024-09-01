/*
package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.card.CreateCardIssueRequest;
import com.shinhan.knockknock.domain.dto.card.CreateCardIssueResponse;
import com.shinhan.knockknock.domain.entity.CardIssueEntity;
import com.shinhan.knockknock.domain.entity.RiskEnum;
import com.shinhan.knockknock.repository.CardIssueRepository;
import com.shinhan.knockknock.service.card.CardIssueServiceImpl;
import com.shinhan.knockknock.service.card.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CardIssueServiceImplTest {

    @Mock
    private CardIssueRepository cardIssueRepository;

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardIssueServiceImpl cardIssueService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("카드 신청 테스트")
    void createPostCardIssue_savesCardIssueEntity() {
        // Given
        CreateCardIssueRequest request = CreateCardIssueRequest.builder()
                .cardIssueFirstEname("John")
                .cardIssueLastEname("Doe")
                .cardIssueEname("John Doe")
                .cardIssueEmail("john.doe@example.com")
                .cardIssueBank("Shinhan Bank")
                .cardIssueAccount("1234-5678-9012")
                .cardIssueIsAgree(true)
                .cardIssueIncome("50000")
                .cardIssueCredit("Good")
                .cardIssueAmountDate("2024-03-01") // 수정된 날짜 형식
                .cardIssueSource("Online")
                .cardIssueIsHighrisk(RiskEnum.valueOf("HIGHRISK"))
                .cardIssuePurpose("Personal")
                .cardIssueAddress("123 Street, City")
                .cardIssueIsFamily(true)
                .cardIssuePassword("password123")
                .build();

        Long userNo = 1L;

        // When
        CreateCardIssueResponse response = cardIssueService.createPostCardIssue(request, userNo);

        // Then
        // 카드 발급 요청이 DB에 저장되는지 확인
        verify(cardIssueRepository, times(1)).save(any(CardIssueEntity.class));

        // 카드 발급 예약 메서드가 호출되는지 확인
        verify(cardService, times(1)).scheduleCreatePostCard(any(CardIssueEntity.class), eq("password123"));

        // 응답 메시지와 상태 코드 확인
        assertEquals("카드 발급 요청이 접수되었습니다.", response.getMessage());
        assertEquals(HttpStatus.ACCEPTED, response.getStatus());
    }
}
*/
