package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRequest;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationResponse;
import com.shinhan.knockknock.service.conversation.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/conversation")
@Tag(name = "말동무", description = "말동무 관련 API")
public class ConversationController {

    @Autowired
    ConversationService conversationService;

    @PostMapping
    @Operation(summary = "말동무 대화 [In Progress]", description = "말동무의 답변을 생성합니다.")
    public ResponseEntity<ConversationResponse> conversation(@RequestBody ConversationRequest request) {
        byte[] audioData = conversationService.conversation(request);

        // 오디오 데이터를 Base64로 인코딩
        String audioBase64 = Base64.getEncoder().encodeToString(audioData);

        // DTO 생성
        ConversationResponse response = new ConversationResponse("Your conversation message here", audioBase64);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
