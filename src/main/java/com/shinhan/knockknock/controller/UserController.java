package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.UserValidationResponse;
import com.shinhan.knockknock.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@Tag(name = "User", description = "User API")
@CrossOrigin
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "아이디 중복확인", description = "회원가입시 아이디 중복확인을 위한 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 조회 성공")
    })
    @GetMapping("/validation/{userId}")
    public ResponseEntity<UserValidationResponse> duplicateCheckUserId(@PathVariable String userId) {
        Boolean result = userService.readUserId(userId);
        String message = "";
        if(result){
            message = "사용가능한 아이디입니다.";
        } else {
            message = "이미 사용중인 아이디입니다.";
        }
        UserValidationResponse response = UserValidationResponse.builder()
                .message(message)
                .result(result)
                .build();
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "SMS 전송", description = "회원가입시 전화번호 인증을 위한 SMS 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 전송 성공 / 실패"),
            @ApiResponse(responseCode = "400", description = "이미 가입된 전화번호")
    })
    @PostMapping("/validation/phone")
    public ResponseEntity<UserValidationResponse> sendSms(@RequestBody Map<String, String> body, HttpSession httpSession) {
        String validationNum = generateRandomNumber();  // 6자리 인증번호 생성
        String phone = body.get("phone");
        String message = "";
        boolean result = false;
        int status = 400;
        boolean isPresentPhone = userService.readUserPhone(phone);

        if (!isPresentPhone) {
            message = "이미 가입된 전화번호입니다.";
        } else {
            SingleMessageSentResponse messageSentResponse = userService.sendSms(phone, validationNum);
            String messageStatus = messageSentResponse.getStatusCode(); // sms 전송 상태
            if (messageStatus.matches("2000|3000|4000")) {  // 제대로 발송된 경우
                httpSession.setAttribute("validationNum", validationNum);
                httpSession.setMaxInactiveInterval(600);    // 10분후 세션만료(인증번호 만료)
                message = "인증번호 전송이 완료되었습니다.";
                result = true;
            } else {
                message = messageSentResponse.getStatusMessage();
            }
            status = 200;
        }

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
    @GetMapping("/validation/phone/{validation}")
    public ResponseEntity<UserValidationResponse> validationSms(@PathVariable String validation, HttpSession httpSession){
        String validationNum = (String)httpSession.getAttribute("validationNum");
        String message = "";
        boolean result = false;
        int status = 400;
        if(validationNum == null) {
            message = "인증번호가 만료되었습니다. 다시 시도해주세요.";
        } else {
            if(validationNum.equals(validation)){
                message = "인증이 완료되었습니다.";
                result = true;
                httpSession.removeAttribute("validationNum");
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

    private String generateRandomNumber() {
        Random random = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            numStr.append(random.nextInt(10));
        }
        return numStr.toString();
    }
}