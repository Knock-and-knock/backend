//package com.shinhan.knockknock.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.shinhan.knockknock.auth.JwtProvider;
//import com.shinhan.knockknock.domain.dto.CreateCardIssueRequest;
//import com.shinhan.knockknock.domain.dto.CreateCardIssueResponse;
//import com.shinhan.knockknock.domain.dto.ReadCardResponse;
//import com.shinhan.knockknock.service.CardIssueService;
//import com.shinhan.knockknock.service.CardService;
//import com.shinhan.knockknock.service.ClovaOCRService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.sql.Date;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@AutoConfigureMockMvc
//@WebMvcTest(CardController.class)
//public class CardControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    // 직렬화에 사용 ( 객체를 json으로 )
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private CardIssueService cardIssueService;
//
//    @MockBean
//    private CardService cardService;
//
//    @MockBean
//    private ClovaOCRService clovaOCRService;
//
////    @Test
////    @WithMockUser(username = "protege02", password = "1234")
////    @DisplayName("발급 요청이 정상적으로 처리되는지 테스트(비동기 작업이기 때문에 처리될 것을 의미하는 202 Accepted로 결과 나와야 성공)")
////    public void createCardIssueTest() throws Exception {
////        /*
////        Given: 테스트를 위한 준비
////        */
////
////        CreateCardIssueRequest request = CreateCardIssueRequest
////                .builder()
////                .cardIssueEname("ASYNC SEUNGGEON")
////                .cardIssueEmail("guny1117@nate.com")
////                .cardIssueBank("카카오뱅크")
////                .cardIssueAccount("1231321312-231")
////                .cardIssueIsAgree(true)
////                .cardIssueIncome("0")
////                .cardIssueCredit("0")
////                .cardIssueAmountDate(Date.valueOf("2024-08-12"))
////                .cardIssueSource("working")
////                .cardIssueIsHighrisk(true)
////                .cardIssuePurpose("use")
////                .userNo(1L)
////                .cardIssueIsFamily(false)
////                .cardIssueAddress("서울시 관악구")
////                .cardIssuePassword("0001")
////                .build();
////
////        CreateCardIssueResponse createCardIssueResponse = CreateCardIssueResponse.builder()
////                .message("카드 발급 요청이 접수되었습니다.")
////                .status(HttpStatus.ACCEPTED)
////                .build();
//
//        // Mockito 라이브러리를 사용하여 서비스 계층 모의(흉내)
//        //Mockito.when(cardIssueService.createPostCardIssue(Mockito.any(CreateCardIssueRequest.class)))
//        //        .thenReturn(createCardIssueResponse);
//
//        /*
//        When: 테스트 행위를 수행
//        */
//        mockMvc.perform(post("/api/v1/card")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON) // 직렬화된 문자열을 MockMvc를 사용하여 Post 요청의 content로 사용
//                        .content(objectMapper.writeValueAsString(request))) // request 객체를 json으로 직렬화
//                /*
//                Then: 결과 검증
//                */
//                .andExpect(status().isAccepted()) // 응답 상태 코드가 202 Accepted인지 검증
//                .andExpect(content().json(objectMapper.writeValueAsString(createCardIssueResponse))); // 서버의 응답이 예상과 일치하는지 검증
//    }
//
//    @Test
//    @WithMockUser(username = "protege02", password = "1234")
//    @DisplayName("사용자 번호로 카드 정보 조회 테스트")
//    public void readDetailTest() throws Exception {
//        /*
//        Given: 테스트를 위한 준비
//        */
//        Long userNo = 1L;
//        List<ReadCardResponse> mockResponse = Arrays.asList(
//                ReadCardResponse.builder()
//                        .cardEname("YangSeungGeon")
//                        .cardBank("카카오뱅크")
//                        .cardAccount("123-456-7890")
//                        .cardAmountDate(Date.valueOf("2024-08-12"))
//                        .cardAddress("서울시 관악구")
//                        .cardIsFamily(true)
//                        .build(),
//                ReadCardResponse.builder()
//                        .cardEname("YangSeungGeon")
//                        .cardBank("신한은행")
//                        .cardAccount("098-765-4321")
//                        .cardAmountDate(Date.valueOf("2024-09-12"))
//                        .cardAddress("부산시 해운대구")
//                        .cardIsFamily(false)
//                        .build()
//        );
//
//        // Mockito 라이브러리를 사용하여 서비스 계층 모의(흉내)
//        Mockito.when(cardService.readGetCards(userNo)).thenReturn(mockResponse);
//
//        /*
//        When: 테스트 행위를 수행
//        */
//        mockMvc.perform(get("/api/v1/card/{userNo}", userNo)
//                        .contentType(MediaType.APPLICATION_JSON))
//                /*
//                Then: 결과 검증
//                */
//                .andExpect(status().isOk()) // 응답 상태 코드가 200 OK인지 검증
//                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse))); // 서버의 응답이 예상과 일치하는지 검증
//    }
//
//
//}