package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.card.CreateCardIssueRequest;
import com.shinhan.knockknock.domain.dto.card.CreateCardIssueResponse;
import com.shinhan.knockknock.service.card.CardIssueService;
import com.shinhan.knockknock.service.card.CardService;
import com.shinhan.knockknock.service.card.ClovaOCRService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CardControllerTest {

    // Mock 객체 정의: 테스트 시 실제 객체 대신 사용될 객체들
    @Mock
    private CardIssueService cardIssueService;

    @Mock
    private CardService cardService;

    @Mock
    private ClovaOCRService clovaOCRService;

    @Mock
    private JwtProvider jwtProvider;

    // 테스트할 클래스에 Mock 객체를 주입
    @InjectMocks
    private CardController cardController;

    // 각 테스트 전에 Mock 객체 초기화
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("카드 발급 요청 처리 메서드 테스트")
    void createCardIssue() {
        // Given
        CreateCardIssueRequest request = new CreateCardIssueRequest();
        CreateCardIssueResponse response = CreateCardIssueResponse.builder()
                .message("카드 발급 요청이 접수되었습니다.")
                .status(HttpStatus.ACCEPTED)
                .build(); // 예상 응답 데이터

        // Mock 객체 동작 설정
        when(jwtProvider.getUserNoFromHeader(anyString())).thenReturn(1L);
        when(cardIssueService.createPostCardIssue(request, 1L)).thenReturn(response);

        // When
        CreateCardIssueResponse result = cardController.createCardIssue("Bearer token", request);

        // Then
        assertEquals(HttpStatus.ACCEPTED, result.getStatus());
        verify(cardIssueService, times(1)).createPostCardIssue(request, 1L); // 카드 발급 서비스가 한 번 호출되었는지 확인
    }

    /*@Test
    @DisplayName("카드 조회 요청 처리 메서드 테스트")
    void readDetail() {
        // Given
        Long userNo = 1L;
        List<ReadCardResponse> responses = Collections.singletonList(new ReadCardResponse()); // 예상 응답 데이터

        // Mock 객체 동작 설정: 카드 서비스가 해당 사용자 번호로 카드 목록을 반환하도록 설정
        when(cardService.readGetCards(userNo)).thenReturn(responses);

        // When
        List<ReadCardResponse> result = cardController.readDetail(userNo);

        // Then
        assertEquals(responses, result); // 반환된 카드 목록이 예상과 일치하는지 확인
        verify(cardService, times(1)).readGetCards(userNo); // 카드 서비스가 정확히 한 번 호출되었는지 확인
    }*/
}
