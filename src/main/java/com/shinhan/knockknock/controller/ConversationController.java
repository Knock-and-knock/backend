package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.conversation.ConversationRequest;
import com.shinhan.knockknock.domain.dto.conversation.ConversationResponse;
import com.shinhan.knockknock.service.conversation.ConversationService;
import com.shinhan.knockknock.util.IpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conversation")
@Tag(name = "3. 말동무", description = "말동무 관련 API")
public class ConversationController {

    private final ConversationService conversationService;
    private final JwtProvider jwtProvider;

    @PostMapping
    @Operation(summary = "말동무 대화 [In Progress]", description = "말동무의 답변을 생성합니다.")
    public ResponseEntity<ConversationResponse> conversation(
            @RequestHeader("Authorization") String header,
            @RequestBody ConversationRequest request,
            HttpServletRequest httpServletRequest) {

        long userNo = jwtProvider.getUserNoFromHeader(header);

        // 사용자 IP 주소 추출
        String clientIp = IpUtil.getClientIp(httpServletRequest);

        log.info("📌 Received conversation request from IP: \u001B[34m{}\u001B[0m, input=\u001B[34m{}\u001B[0m, conversationRoomNo=\u001B[34m{}\u001B[0m",
                clientIp, request.getInput(), request.getConversationRoomNo());

        long startTime = System.currentTimeMillis();
        ConversationResponse response = conversationService.conversation(request, userNo);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("📌 Chatbot response: totalTokens=\u001B[34m{}\u001B[0m, duration=\u001B[34m{}ms\u001B[0m", response.getTotalTokens(), duration);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping("/test")
    @Operation(summary = "말동무 대화 test [In Progress]", description = "말동무의 답변을 생성합니다.")
    public ResponseEntity<ConversationResponse> conversationTest(
            @RequestHeader("Authorization") String header,
            @RequestBody ConversationRequest request,
            HttpServletRequest httpServletRequest) {

        long userNo = jwtProvider.getUserNoFromHeader(header);

        // 사용자 IP 주소 추출
        String clientIp = IpUtil.getClientIp(httpServletRequest);

        log.info("📌 Received conversation request from IP: \u001B[34m{}\u001B[0m, input=\u001B[34m{}\u001B[0m, conversationRoomNo=\u001B[34m{}\u001B[0m",
                clientIp, request.getInput(), request.getConversationRoomNo());

        long startTime = System.currentTimeMillis();
        ConversationResponse response = conversationService.conversation(request, userNo);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("📌 Chatbot response: totalTokens=\u001B[34m{}\u001B[0m, duration=\u001B[34m{}ms\u001B[0m", response.getTotalTokens(), duration);

        ConversationResponse.ReservationResult reservationTest= ConversationResponse.ReservationResult.builder()
                .welfareNo(1)
                .welfareBookStartDate("2024-09-06")
                .welfareBookEndDate("2024-09-06")
                .welfareBookUseTime(1)
                .build();

        response.setActionRequired(true);
        response.setReservationResult(reservationTest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
