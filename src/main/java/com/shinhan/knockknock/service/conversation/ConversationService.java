package com.shinhan.knockknock.service.conversation;

import com.shinhan.knockknock.domain.dto.conversationroom.ChatbotResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationLogRequest;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRequest;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;


@Service
@Slf4j
public class ConversationService {

    @Autowired
    TextResponseService textResponseService;

    @Autowired
    TextToSpeechService textToSpeechService;

    @Autowired
    ConversationLogService conversationLogService;

    @Autowired
    ConversationRoomService conversationRoomService;

    public ConversationResponse conversation(ConversationRequest request) {
        log.info("📌 Received conversation request: input={}, conversationRoomNo={}", request.getInput(), request.getConversationRoomNo());

        // Chatbot 답변 생성
        ChatbotResponse response = textResponseService.TextResponse(request);

        // 대화 내역 저장
        ConversationLogRequest conversationLog = ConversationLogRequest.builder()
                .conversationLogInput(request.getInput())
                .conversationLogResponse(response.getContent())
                .conversationLogToken(response.getTotalTokens())
                .conversationRoomNo(request.getConversationRoomNo())
                .build();
        conversationLogService.createConversationLog(conversationLog);
        conversationRoomService.updateConversationRoomEndAt(request.getConversationRoomNo());

        // 음성 데이터 생성
        byte[] audioData = textToSpeechService.convertTextToSpeech(response.getContent());

        log.info("📌 Chatbot response: content={}, totalTokens={}", response.getContent(), response.getTotalTokens());

        // 오디오 데이터를 Base64로 인코딩
        String audioBase64 = Base64.getEncoder().encodeToString(audioData);

        return ConversationResponse.builder()
                .content(response.getContent())
                .audioData(audioBase64)
                .actionRequired(response.isActionRequired())
                .build();
    }

}
