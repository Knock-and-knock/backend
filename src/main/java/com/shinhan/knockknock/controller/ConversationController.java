package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRequest;
import com.shinhan.knockknock.service.ConversationService;
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

@RestController
@RequestMapping("/api/v1/conversation")
@Tag(name = "말동무", description = "말동무 관련 API 입니다.")
public class ConversationController {

    @Autowired
    ConversationService conversationService;

    @PostMapping
    public ResponseEntity<byte[]> conversation(@RequestBody ConversationRequest request) {
        byte[] audioData = conversationService.conversation(request);

        StreamingResponseBody stream = outputStream -> {
            // 스트림으로 데이터를 전송
            try (OutputStream os = outputStream) {
                os.write(audioData);
                os.flush();
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/wav"));
        headers.setContentDispositionFormData("attachment", "speech.wav");
        return new ResponseEntity<>(audioData, headers, HttpStatus.OK);
    }
}
