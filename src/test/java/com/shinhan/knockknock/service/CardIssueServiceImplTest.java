//package com.shinhan.knockknock.service;
//
//import com.shinhan.knockknock.domain.dto.CreateCardIssueRequest;
//import com.shinhan.knockknock.domain.dto.CreateCardIssueResponse;
//import com.shinhan.knockknock.domain.entity.CardIssueEntity;
//import com.shinhan.knockknock.repository.CardIssueRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//import java.sql.Date;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//
//@SpringJUnitConfig(CardIssueServiceImpl.class)
//public class CardIssueServiceImplTest {
//
//    @Autowired
//    private CardIssueServiceImpl cardIssueService;
//
//    @MockBean
//    private CardIssueRepository cardIssueRepository;
//
//    @MockBean
//    private CardServiceImpl cardService;
//
//    @Test
//    @DisplayName("createPostCardIssue 메소드 테스트")
//    public void createPostCardIssueTest() {
//        /*
//        Given: 테스트 준비
//        */
//        CreateCardIssueRequest request = CreateCardIssueRequest
//                .builder()
//                .cardIssueResidentNo("987654-1234567")
//                .cardIssueEname("ASYNC SEUNGGEON")
//                .cardIssueEmail("guny1117@nate.com")
//                .cardIssueBank("카카오뱅크")
//                .cardIssueAccount("1231321312-231")
//                .cardIssueIsAgree(true)
//                .cardIssueIncome(0)
//                .cardIssueCredit(0)
//                .cardIssueAmountDate(Date.valueOf("2024-08-12"))
//                .cardIssueSource("working")
//                .cardIssueIsHighrisk(true)
//                .cardIssuePurpose("use")
//                .userNo(1L)
//                .cardIssueIsFamily(false)
//                .cardIssueAddress("서울시 관악구")
//                .cardIssuePassword("0001")
//                .build();
//
//        CardIssueEntity cardIssueEntity = CardIssueEntity.builder().build();
//        Mockito.when(cardIssueRepository.save(any(CardIssueEntity.class))).thenReturn(cardIssueEntity);
//
//        /*
//        When: 테스트 수행
//        */
//        CreateCardIssueResponse response = cardIssueService.createPostCardIssue(request, 1L);
//
//        /*
//        Then: 결과 검증
//        */
//        assertEquals("카드 발급 요청이 접수되었습니다.", response.getMessage());
//        assertEquals(HttpStatus.ACCEPTED, response.getStatus());
//
//        Mockito.verify(cardIssueRepository).save(any(CardIssueEntity.class));
//        Mockito.verify(cardService).scheduleCreatePostCard(cardIssueEntity, "0001");
//    }
//}
