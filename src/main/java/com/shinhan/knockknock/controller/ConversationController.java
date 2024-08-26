package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRequest;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationResponse;
import com.shinhan.knockknock.service.conversation.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conversation")
@Tag(name = "말동무", description = "말동무 관련 API")
public class ConversationController {

    private final ConversationService conversationService;
    private final JwtProvider jwtProvider;

    @PostMapping
    @Operation(summary = "말동무 대화 [In Progress]", description = "말동무의 답변을 생성합니다.")
    public ResponseEntity<ConversationResponse> conversation(@RequestHeader("Authorization") String header, @RequestBody ConversationRequest request) {
        Long userNo = jwtProvider.getUserNoFromHeader(header);

        ConversationResponse response = conversationService.conversation(request, userNo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
