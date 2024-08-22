//package com.shinhan.knockknock.service;
//
//import com.shinhan.knockknock.domain.dto.CreateCardIssueResponse;
//import com.shinhan.knockknock.domain.dto.ReadCardResponse;
//import com.shinhan.knockknock.domain.entity.CardEntity;
//import com.shinhan.knockknock.domain.entity.CardIssueEntity;
//import com.shinhan.knockknock.repository.CardIssueRepository;
//import com.shinhan.knockknock.repository.CardRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.scheduling.annotation.AsyncResult;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//import java.sql.Date;
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//
//@SpringJUnitConfig(CardServiceImpl.class)
//public class CardServiceImplTest {
//
//    @Autowired
//    private CardServiceImpl cardService;
//
//    @MockBean
//    private CardRepository cardRepository;
//
//    @MockBean
//    private CardIssueRepository cardIssueRepository;
//
//    @Test
//    @DisplayName("scheduleCreatePostCard 메소드 테스트")
//    public void scheduleCreatePostCardTest() throws Exception {
//        /*
//        Given: 테스트 준비
//        */
//        CardIssueEntity cardIssueEntity = CardIssueEntity.builder().build();
//        String password = "0001";
//
//        /*
//        When: 테스트 수행
//        */
//        CompletableFuture<CreateCardIssueResponse> future = new AsyncResult<>(cardService.createPostCard(cardIssueEntity, password)).completable();
//        future.get(); // 비동기 업무가 완료될 때까지 테스트
//
//        /*
//        Then: 결과 검증
//        */
//        Mockito.verify(cardRepository, Mockito.times(1)).save(any(CardEntity.class));
//    }
//
//    @Test
//    @DisplayName("createPostCard 메소드 테스트")
//    public void createPostCardTest() {
//        /*
//        Given: 테스트 준비
//        */
//        CardIssueEntity cardIssueEntity = CardIssueEntity.builder()
//                .cardIssueEname("ASYNC SEUNGGEON")
//                .cardIssueBank("카카오뱅크")
//                .cardIssueAccount("1231321312-231")
//                .cardIssueAmountDate(Date.valueOf("2024-08-12"))
//                .build();
//
//        Mockito.when(cardRepository.save(any(CardEntity.class)))
//                .thenReturn(CardEntity.builder().build());
//
//        /*
//        When: 테스트 수행
//        */
//        CreateCardIssueResponse response = cardService.createPostCard(cardIssueEntity, "0001");
//
//        /*
//        Then: 결과 검증
//        */
//        assertEquals("카드 발급 성공", response.getMessage());
//        assertEquals(HttpStatus.CREATED, response.getStatus());
//
//        // Verify the interaction with the mock repository
//        Mockito.verify(cardRepository).save(any(CardEntity.class));
//    }
//
//    @Test
//    @DisplayName("readGetCards 메소드 테스트")
//    public void readGetCardsTest() {
//        /*
//        Given: 테스트 준비
//        */
//        Long userNo = 1L;
//        List<CardEntity> cardEntities = Arrays.asList(
//                CardEntity.builder()
//                        .cardEname("YangSeungGeon")
//                        .cardBank("카카오뱅크")
//                        .cardAccount("123-456-7890")
//                        .cardAmountDate(Date.valueOf("2024-08-12"))
//                        .cardExpiredate(Date.valueOf(LocalDate.now().plusYears(5)))
//                        .cardIsfamily(true)
//                        .build(),
//                CardEntity.builder()
//                        .cardEname("YangSeungGeon")
//                        .cardBank("신한은행")
//                        .cardAccount("098-765-4321")
//                        .cardAmountDate(Date.valueOf("2024-09-12"))
//                        .cardExpiredate(Date.valueOf(LocalDate.now().plusYears(5)))
//                        .cardIsfamily(false)
//                        .build()
//        );
//
//        Mockito.when(cardRepository.findByUserNo(userNo)).thenReturn(cardEntities);
//
//        /*
//        When: 테스트 수행
//        */
//        List<ReadCardResponse> response = cardService.readGetCards(userNo);
//
//        /*
//        Then: 결과 검증
//        */
//        assertEquals(2, response.size());
//        assertEquals("YangSeungGeon", response.get(0).getCardEname());
//        assertEquals("카카오뱅크", response.get(0).getCardBank());
//
//        // Verify the interaction with the mock repository
//        Mockito.verify(cardRepository).findByUserNo(userNo);
//    }
//}