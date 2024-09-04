//package com.shinhan.knockknock.service;
//
//import com.shinhan.knockknock.domain.dto.user.CreateUserRequest;
//import com.shinhan.knockknock.domain.dto.welfare.CreateWelfareRequest;
//import com.shinhan.knockknock.domain.dto.welfarebook.CreateWelfareBookRequest;
//import com.shinhan.knockknock.domain.dto.welfarebook.ReadWelfareBookResponse;
//import com.shinhan.knockknock.service.user.UserService;
//import com.shinhan.knockknock.service.welfare.WelfareService;
//import com.shinhan.knockknock.service.welfarebook.WelfareBookService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.sql.Date;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ActiveProfiles("test")
//@SpringBootTest
//@Transactional
//public class WelfareBookServiceTest {
//
//    @Autowired
//    WelfareBookService welfareBookService;
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    WelfareService welfareService;
//
//    @Test
//    @DisplayName("돌봄 예약 생성 테스트")
//    public void createWelfareBookTest() {
//        // Given
//        String userId = "test01";
//        CreateUserRequest request = CreateUserRequest.builder()
//                .userId(userId)
//                .userPassword("1234")
//                .userName("테스트01")
//                .userPhone("01012121212")
//                .userSimplePassword("123456")
//                .isBioLogin(false)
//                .build();
//        userService.createUser(request);
//        Long userNo = userService.readByUserId(userId);
//
//        CreateWelfareRequest welfareRequest = CreateWelfareRequest.builder()
//                .welfareName("건강 보험")
//                .welfarePirce(1000L)
//                .welfareCategory("건강")
//                .build();
//        Long welfareNo = welfareService.createWelfare(welfareRequest);
//
//        CreateWelfareBookRequest welfareBookRequest = CreateWelfareBookRequest.builder()
//                .welfareBookStartDate(Date.valueOf("2024-08-08"))
//                .welfareBookEndDate(Date.valueOf("2024-08-08"))
//                .welfareBookIsCansle(false)
//                .welfareBookIsComplete(false)
//                .userNo(userNo)
//                .userBirth(Date.valueOf("1955-08-13"))
//                .userAddress("서울시 마포구 월드컵대로")
//                .userGender(2)
//                .userHeight(161)
//                .userWeight(54)
//                .userDisease("고혈압")
//                .welfareNo(welfareNo)
//                .welfareBookUseTime(3)
//                .build();
//
//        // When
//        welfareBookService.createWelfareBook(welfareBookRequest, userNo);
//
//        // Then
//        List<ReadWelfareBookResponse> bookList =  welfareBookService.readAllByUserNo(userNo);
//        System.out.println(bookList);
//        assertThat(bookList).isNotNull();
//    }
//}
