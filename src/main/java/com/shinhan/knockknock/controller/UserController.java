package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.user.*;
import com.shinhan.knockknock.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Random;

@Tag(name = "1. 회원", description = "회원 API")
@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private static final HashMap<String, String> validationMap = new HashMap<>();

    @Operation(summary = "아이디 중복확인", description = "회원가입시 아이디 중복확인을 위한 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 조회 성공")
    })
    @GetMapping("/validation/{userId}")
    public ResponseEntity<UserValidationResponse> duplicateCheckUserId(@PathVariable String userId) {
        Boolean result = false;
        String message = "";
        int status = 200;
        try {
            result = userService.readUserId(userId);
            if (result) {
                message = "사용가능한 아이디입니다.";
            } else {
                message = "이미 사용중인 아이디입니다.";
            }
        } catch (Exception e) {
            message = e.getMessage();
            status = 400;
        }

        UserValidationResponse response = UserValidationResponse.builder()
                .message(message)
                .result(result)
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "SMS 전송", description = "회원가입시 전화번호 인증을 위한 SMS 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 전송 성공 / 실패"),
            @ApiResponse(responseCode = "400", description = "이미 가입된 전화번호")
    })
    @PostMapping("/validation/phone")
    public ResponseEntity<UserValidationResponse> sendSms(@RequestBody UserValidationRequest request) {
        String validationNum = generateRandomNumber();  // 6자리 인증번호 생성
        String phone = request.getPhone();
        String message = "";
        boolean result = false;
        int status = 400;

        SingleMessageSentResponse messageSentResponse = userService.sendSms(phone, validationNum);
        String messageStatus = messageSentResponse.getStatusCode(); // sms 전송 상태
        if (messageStatus.matches("2000|3000|4000")) {  // 제대로 발송된 경우
            validationMap.put(phone, validationNum);
            message = "인증번호 전송이 완료되었습니다.";
            result = true;
        } else {
            message = messageSentResponse.getStatusMessage();
        }
        status = 200;

        UserValidationResponse response = UserValidationResponse.builder()
                .message(message)
                .result(result)
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "인증번호 검증", description = "회원가입시 전화번호 인증을 위한 인증번호 검증")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 완료 / 실패"),
            @ApiResponse(responseCode = "400", description = "만료된 인증번호")
    })
    @PostMapping("/validation/number")
    public ResponseEntity<UserValidationResponse> validationSms(@RequestBody UserValidationRequest request) {
        /* 세션 활용 전화번호 인증
        HttpSession httpSession = request.getSession(false);
        String validationNum = (String)httpSession.getAttribute("validationNum");*/
        String validation = request.getValidationNum();
        String validationNum = validationMap.get(request.getPhone());
        String message = "";
        boolean result = false;
        int status = 400;
        if (validationNum == null) {
            message = "인증번호가 만료되었습니다. 다시 시도해주세요.";
        } else {
            if (validationNum.equals(validation)) {
                message = "인증이 완료되었습니다.";
                result = true;
                validationMap.remove(request.getPhone());
                //httpSession.removeAttribute("validationNum");
            } else {
                message = "잘못된 인증번호입니다.";
            }
            status = 200;
        }
        UserValidationResponse response = UserValidationResponse.builder()
                .message(message)
                .result(result)
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "회원가입", description = "회원가입 처리 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가입 성공 / 실패")
    })
    @PostMapping("/signup")
    public ResponseEntity<CreateUserResponse> create(@RequestBody CreateUserRequest request) {
        String message = "";
        int status = 200;
        CreateUserResponse response = new CreateUserResponse();
        try {
            response = userService.createUser(request);
            message = "회원가입에 성공하였습니다.";
        } catch (DuplicateKeyException e) {
            message = e.getMessage();
            status = 409;
        } catch (DataIntegrityViolationException e) {
            message = e.getMessage();
            status = 400;
        } catch (Exception e) {
            message = e.getMessage();
            status = 500;
        }

        response.setMessage(message);
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "회원조회", description = "회원조회 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 조회 성공"),
            @ApiResponse(responseCode = "400", description = "회원 조회 실패")
    })
    @GetMapping()
    public ResponseEntity<?> readUser(@RequestHeader("Authorization") String header) {
        long userNo = jwtProvider.getUserNoFromHeader(header);
        try {
            ReadUserResponse response = userService.readUser(userNo);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(UserValidationResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @Operation(summary = "회원 정보 수정", description = "마이페이지 부가 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 수정 성공"),
            @ApiResponse(responseCode = "404", description = "회원 조회 실패"),
            @ApiResponse(responseCode = "400", description = "정보 수정 불가")
    })
    @PutMapping
    public ResponseEntity<ReadUserResponse> updateUser(
            @RequestHeader("Authorization") String header,
            @RequestBody UpdateUserRequest request) {
        long userNo = jwtProvider.getUserNoFromHeader(header);
        int status = 400;
        String message = "";
        try {
            ReadUserResponse response = userService.updateUser(userNo, request);
            return ResponseEntity.status(200).body(response);
        } catch (NoSuchElementException e) {
            status = 404;
            message = e.getMessage();
        } catch (Exception e){
            message = e.getMessage();
        }

        return ResponseEntity.status(status).body(ReadUserResponse.builder()
                .message(message)
                .build());
    }

    @Operation(summary = "회원 탈퇴", description = "마이페이지 회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "탈퇴 완료 / 실패"),
            @ApiResponse(responseCode = "404", description = "회원 조회 실패"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/withdraw")
    public ResponseEntity<UserValidationResponse> deleteUser(@RequestHeader("Authorization") String header) {
        long userNo = jwtProvider.getUserNoFromHeader(header);
        int status = 400;
        String message = "";
        Boolean result = false;
        try {
            result = userService.deleteUser(userNo);
            status = 200;
            message = result?"탈퇴가 완료되었습니다.": "탈퇴에 실패하였습니다.";
        } catch (NoSuchElementException e) {
            status = 404;
            message = e.getMessage();
        } catch (Exception e) {
            message = e.getMessage();
        }

        return ResponseEntity.status(status).body(UserValidationResponse.builder()
                .message(message)
                .result(result)
                .build());
    }

    @Operation(summary = "간편 결제 비밀번호 등록 여부 조회", description = "간편 결제 비밀번호 등록 여부 조회 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/payment")
    public ResponseEntity<SimplePaymentResponse> readPaymentPassword(@RequestHeader("Authorization") String header) {
        long userNo = jwtProvider.getUserNoFromHeader(header);
        SimplePaymentResponse response = null;
        int status = 400;

        try {
            response = userService.readSimplePayment(userNo);
            status = 200;
        } catch (Exception e) {
            response = SimplePaymentResponse.builder()
                    .message("조회 중 오류가 발생하였습니다.")
                    .result(false)
                    .build();
        }

        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "간편 결제 비밀번호 등록", description = "간편 결제 비밀번호 등록 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/payment")
    public ResponseEntity<SimplePaymentResponse> createPaymentPassword(@RequestHeader("Authorization") String header,
                                                                       @RequestBody SimplePaymentRequest request) {
        long userNo = jwtProvider.getUserNoFromHeader(header);
        SimplePaymentResponse response = null;
        int status = 400;
        try {
            response = userService.createSimplePayment(userNo, request);
            status = 200;
        } catch (Exception e) {
            response = SimplePaymentResponse.builder()
                    .message(e.getMessage())
                    .result(false)
                    .build();
        }

        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "간편 결제 비밀번호 검증", description = "간편 결제 비밀번호 검증 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검증 성공"),
            @ApiResponse(responseCode = "400", description = "검증 실패")
    })
    @PostMapping("/payment")
    public ResponseEntity<SimplePaymentResponse> createPayment(@RequestHeader("Authorization") String header,
                                                               @RequestBody SimplePaymentRequest request) {
        long userNo = jwtProvider.getUserNoFromHeader(header);
        SimplePaymentResponse response = null;
        int status = 400;
        try {
            response = userService.validateSimplePayment(userNo, request);
            status = 200;
        } catch (Exception e) {
            response = SimplePaymentResponse.builder()
                    .message(e.getMessage())
                    .result(false)
                    .build();
        }
        return ResponseEntity.status(status).body(response);
    }

    private String generateRandomNumber() {
        Random random = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            numStr.append(random.nextInt(10));
        }
        return numStr.toString();
    }
}