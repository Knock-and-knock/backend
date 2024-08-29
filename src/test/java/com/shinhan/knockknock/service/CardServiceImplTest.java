//package com.shinhan.knockknock.service;
//
//import com.shinhan.knockknock.domain.dto.card.ReadCardResponse;
//import com.shinhan.knockknock.domain.entity.CardEntity;
//import com.shinhan.knockknock.domain.entity.CardIssueEntity;
//import com.shinhan.knockknock.repository.CardIssueRepository;
//import com.shinhan.knockknock.repository.CardRepository;
//import com.shinhan.knockknock.service.card.CardServiceImpl;
//import com.shinhan.knockknock.service.notification.NotificationServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.sql.Date;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class CardServiceImplTest {
//
//    // Mock 객체 정의: 실제 동작 대신 테스트에서 사용될 객체들
//    @Mock
//    private CardRepository cardRepository;
//
//    @Mock
//    private CardIssueRepository cardIssueRepository;
//
//    @Mock
//    private NotificationServiceImpl notificationService;
//
//    // Mock 객체들이 주입될 클래스 인스턴스
//    @InjectMocks
//    private CardServiceImpl cardService;
//
//    // 각 테스트 전에 Mock 객체를 초기화
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    @DisplayName("카드 발급 예약 메서드 테스트")
//    // 스케줄링 작업이 올바르게 설정되었는지만 확인
//    void scheduleCreatePostCard() {
//        // Given
//        CardIssueEntity cardIssueEntity = new CardIssueEntity();
//        String password = "1234";
//
//        // When
//        cardService.scheduleCreatePostCard(cardIssueEntity, password);
//
//        // Then: 비동기 작업이므로 여기서는 동작만 확인
//        // 10초 후에 실제 카드가 생성되지만, Mock 설정으로 인해 이 동작은 생략
//        verify(cardRepository, never()).save(any(CardEntity.class)); // 실제로 save가 호출되지 않았음을 확인
//    }
//
//    @Test
//    @DisplayName("카드 조회 메서드 테스트: 발급된 카드가 없는 경우")
//    void readGetCardsNoCardsIssued() {
//        // Given
//        Long userNo = 1L;
//        when(cardRepository.findByUserNo(userNo)).thenReturn(Collections.emptyList()); // 발급된 카드가 없도록 설정
//        when(cardIssueRepository.countByUserNo(userNo)).thenReturn(0); // 발급 요청도 없도록 설정
//
//        // When
//        List<ReadCardResponse> result = cardService.readGetCards(userNo);
//
//        // Then
//        assertEquals("발급된 카드가 없습니다.", result.get(0).getCardResponseMessage()); // 응답 메시지 확인
//        verify(cardRepository, times(1)).findByUserNo(userNo); // 카드 조회가 한 번 호출되었는지 확인
//        verify(cardIssueRepository, times(1)).countByUserNo(userNo); // 발급 요청 조회도 한 번 호출되었는지 확인
//    }
//
//    @DisplayName("카드 조회 메서드 테스트: 카드 발급 대기중인 경우")
//    @Test
//    void readGetCardsCardIssuingPending() {
//        // Given
//        Long userNo = 1L;
//        when(cardRepository.findByUserNo(userNo)).thenReturn(Collections.emptyList()); // 발급된 카드가 없도록 설정
//        when(cardIssueRepository.countByUserNo(userNo)).thenReturn(1); // 발급 요청이 1건 있도록 설정
//
//        // When
//        List<ReadCardResponse> result = cardService.readGetCards(userNo);
//
//        // Then
//        assertEquals("카드 발급이 대기 중입니다.", result.get(0).getCardResponseMessage()); // 응답 메시지 확인
//        verify(cardRepository, times(1)).findByUserNo(userNo); // 카드 조회가 한 번 호출되었는지 확인
//        verify(cardIssueRepository, times(1)).countByUserNo(userNo); // 발급 요청 조회도 한 번 호출되었는지 확인
//    }
//
//    @DisplayName("카드 조회 메서드 테스트: 카드가 존재하는 경우")
//    @Test
//    void readGetCardsCardIssued() {
//        // Given
//        Long userNo = 1L;
//        CardEntity cardEntity = new CardEntity();
//        cardEntity.setCardExpiredate(Date.valueOf("2029-08-25"));
//
//        when(cardRepository.findByUserNo(userNo)).thenReturn(Collections.singletonList(cardEntity)); // 발급된 카드가 반환되도록 설정
//
//        // When
//        List<ReadCardResponse> result = cardService.readGetCards(userNo);
//
//        // Then
//        assertEquals(1, result.size()); // 반환된 카드 목록의 사이즈 확인
//        assertEquals("29/08", result.get(0).getCardExpiredate()); // 만료일자 형식이 변환되었는지 확인
//        verify(cardRepository, times(1)).findByUserNo(userNo); // 카드 조회가 정확히 한 번 호출되었는지 확인
//    }
//}
