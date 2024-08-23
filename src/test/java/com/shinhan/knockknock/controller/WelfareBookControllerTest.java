//package com.shinhan.knockknock.controller;
//
//import com.shinhan.knockknock.auth.JwtProvider;
//import com.shinhan.knockknock.domain.dto.welfarebook.CreateWelfareBookRequest;
//import com.shinhan.knockknock.domain.dto.welfarebook.ReadWelfareBookResponse;
//import com.shinhan.knockknock.service.welfarebook.WelfareBookService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.Arrays;
//import java.util.NoSuchElementException;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ActiveProfiles("test")
//@WebMvcTest(WelfareBookController.class)
//public class WelfareBookControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private WelfareBookService welfareBookService;
//
//    @MockBean
//    private JwtProvider jwtProvider;
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    @BeforeEach
//    public void setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }
//
//    @Test
//    @DisplayName("복지 예약 전체 조회 성공 테스트")
//    public void testReadAllByUserNo_Success() throws Exception {
//        // given: 테스트에 필요한 데이터와 Mock 동작 정의
//        Long userNo = 1L;
//        ReadWelfareBookResponse response1 = new ReadWelfareBookResponse();
//        ReadWelfareBookResponse response2 = new ReadWelfareBookResponse();
//
//        // JWT 헤더에서 유저 번호 추출
//        when(jwtProvider.getUserNoFromHeader(any())).thenReturn(userNo);
//        // 유저 번호로 복지 예약 내역 조회
//        when(welfareBookService.readAllByUserNo(userNo)).thenReturn(Arrays.asList(response1, response2));
//
//        // when: 실제로 API 호출
//        // then: 예상한 응답 상태와 결과를 검증
//        mockMvc.perform(get("/api/v1/welfare-book")
//                        .header("Authorization", "Bearer token"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2)); // 응답 리스트의 길이가 2인지 확인
//    }
//
//    @Test
//    @DisplayName("복지 예약 전체 조회 실패 테스트 - 예약 내역을 찾을 수 없음")
//    public void testReadAllByUserNo_NotFound() throws Exception {
//        // given: 유저 번호와 예외 상황 정의
//        when(jwtProvider.getUserNoFromHeader(any())).thenReturn(1L);
//        when(welfareBookService.readAllByUserNo(anyLong())).thenThrow(new NoSuchElementException("Welfare book not found"));
//
//        // when: API 호출
//        // then: 예상한 상태 코드와 예외 메시지를 검증
//        mockMvc.perform(get("/api/v1/welfare-book")
//                        .header("Authorization", "Bearer token"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Welfare book not found"));
//    }
//
//    @Test
//    @DisplayName("복지 예약 상세 조회 성공 테스트")
//    public void testReadDetail_Success() throws Exception {
//        // given: 상세 조회를 위한 데이터 정의
//        ReadWelfareBookResponse response = new ReadWelfareBookResponse();
//        when(welfareBookService.readDetail(anyLong())).thenReturn(response);
//
//        // when: API 호출
//        // then: 정상적으로 상세 데이터가 반환되는지 검증
//        mockMvc.perform(get("/api/v1/welfare-book/{welfareBookNo}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").exists()); // JSON 응답의 내용이 존재하는지 확인
//    }
//
//    @Test
//    @DisplayName("복지 예약 상세 조회 실패 테스트 - 예약 내역을 찾을 수 없음")
//    public void testReadDetail_NotFound() throws Exception {
//        // given: 상세 조회 시 발생하는 예외 정의
//        when(welfareBookService.readDetail(anyLong())).thenThrow(new NoSuchElementException("Welfare book not found"));
//
//        // when: API 호출
//        // then: 예상한 예외 메시지와 상태 코드 검증
//        mockMvc.perform(get("/api/v1/welfare-book/{welfareBookNo}", 1L))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Welfare book not found"));
//    }
//
//    @Test
//    @DisplayName("복지 예약 생성 성공 테스트")
//    public void testCreate_Success() throws Exception {
//        // given: 복지 예약 생성 요청 데이터 정의
//        CreateWelfareBookRequest request = new CreateWelfareBookRequest();
//        when(jwtProvider.getUserNoFromHeader(any())).thenReturn(1L);
//        when(welfareBookService.createWelfareBook(any(), anyLong())).thenReturn(1L);
//
//        // when: API 호출
//        // then: 복지 예약이 성공적으로 생성되었는지 검증
//        mockMvc.perform(post("/api/v1/welfare-book")
//                        .header("Authorization", "Bearer token")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"exampleField\": \"exampleValue\"}"))
//                .andExpect(status().isCreated())
//                .andExpect(content().string("1"));
//    }
//
//    @Test
//    @DisplayName("복지 예약 생성 실패 테스트 - 서버 오류")
//    public void testCreate_InternalServerError() throws Exception {
//        // given: 서버 오류 상황 정의
//        when(jwtProvider.getUserNoFromHeader(any())).thenReturn(1L);
//        when(welfareBookService.createWelfareBook(any(), anyLong())).thenThrow(new RuntimeException());
//
//        // when: API 호출
//        // then: 서버 오류 상태 코드 검증
//        mockMvc.perform(post("/api/v1/welfare-book")
//                        .header("Authorization", "Bearer token")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"exampleField\": \"exampleValue\"}"))
//                .andExpect(status().isInternalServerError());
//    }
//
////    @Test
////    @DisplayName("보호자가 매칭된 사용자를 대신 복지 예약 성공 테스트")
////    public void testCreateForProtege_Success() throws Exception {
////        // given: 복지 예약 생성 요청 데이터 정의
////        CreateWelfareBookRequest request = new CreateWelfareBookRequest();
////        when(jwtProvider.getUserNoFromHeader(any())).thenReturn(1L);  // 보호자 userNo
////        when(welfareBookService.createWelfareBookForProtege(any(), anyLong())).thenReturn(1L);
////
////        // when: API 호출
////        // then: 복지 예약이 성공적으로 생성되었는지 검증
////        mockMvc.perform(post("/api/v1/welfare-book/protege")
////                        .header("Authorization", "Bearer token")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content("{\"exampleField\": \"exampleValue\"}"))
////                .andExpect(status().isCreated())
////                .andExpect(content().string("1"));
////    }
//
////    @Test
////    @DisplayName("보호자가 매칭된 사용자를 대신 복지 예약 실패 테스트 - 서버 오류")
////    public void testCreateForProtege_InternalServerError() throws Exception {
////        // given: 서버 오류 상황 정의
////        when(jwtProvider.getUserNoFromHeader(any())).thenReturn(1L);  // 보호자 userNo
////        when(welfareBookService.createWelfareBookForProtege(any(), anyLong())).thenThrow(new RuntimeException());
////
////        // when: API 호출
////        // then: 서버 오류 상태 코드 검증
////        mockMvc.perform(post("/api/v1/welfare-book/protege")
////                        .header("Authorization", "Bearer token")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content("{\"exampleField\": \"exampleValue\"}"))
////                .andExpect(status().isInternalServerError());
////    }
//
//    @Test
//    @DisplayName("복지 예약 취소 성공 테스트")
//    public void testDelete_Success() throws Exception {
//        // when: 삭제 API 호출
//        // then: 복지 예약이 성공적으로 취소되었는지 검증
//        mockMvc.perform(delete("/api/v1/welfare-book/{welfareBookNo}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(content().string("복지 예약이 성공적으로 취소되었습니다."));
//    }
//
//    @Test
//    @DisplayName("복지 예약 취소 실패 테스트 - 예약 내역을 찾을 수 없음")
//    public void testDelete_NotFound() throws Exception {
//        // given: 삭제 시 발생하는 예외 정의
//        doThrow(new NoSuchElementException("Welfare book not found")).when(welfareBookService).deleteWelfareBook(anyLong());
//
//        // when: API 호출
//        // then: 예외 메시지와 상태 코드 검증
//        mockMvc.perform(delete("/api/v1/welfare-book/{welfareBookNo}", 1L))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Welfare book not found"));
//    }
//
//    @Test
//    @DisplayName("복지 예약 취소 실패 테스트 - 서버 오류")
//    public void testDelete_InternalServerError() throws Exception {
//        // given: 삭제 시 발생하는 서버 오류 정의
//        doThrow(new RuntimeException()).when(welfareBookService).deleteWelfareBook(anyLong());
//
//        // when: API 호출
//        // then: 서버 오류 상태 코드와 메시지 검증
//        mockMvc.perform(delete("/api/v1/welfare-book/{welfareBookNo}", 1L))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().string("복지 예약 취소에 실패했습니다."));
//    }
//}
