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

        if (request.getInput().isEmpty()) {
            return ConversationResponse.builder()
                    .content("err")
                    .build();
        }

        // Chatbot 답변 생성
        ChatbotResponse response = textResponseService.TextResponse(request);

        // Chatbot 답변 검사
        ConversationLogRequest conversationLog;
        if (response.getContent().isEmpty()) {
            log.warn("⚠️ Chatbot response is empty: content={}, totalTokens={}", response.getContent(), response.getTotalTokens());
            conversationLog = ConversationLogRequest.builder()
                    .conversationLogInput(request.getInput())
                    .conversationLogResponse("문제가 발생했습니다. 다시 한번 말해주세요.")
                    .conversationLogToken(response.getTotalTokens())
                    .conversationRoomNo(request.getConversationRoomNo())
                    .build();
        } else {
            conversationLog = ConversationLogRequest.builder()
                    .conversationLogInput(request.getInput())
                    .conversationLogResponse(response.getContent())
                    .conversationLogToken(response.getTotalTokens())
                    .conversationRoomNo(request.getConversationRoomNo())
                    .build();
        }

        // 대화 내역 저장
        conversationLogService.createConversationLog(conversationLog);
        conversationRoomService.updateConversationRoomEndAt(request.getConversationRoomNo());

        // 음성 데이터 생성
        byte[] audioData = textToSpeechService.convertTextToSpeech(conversationLog.getConversationLogResponse());

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
