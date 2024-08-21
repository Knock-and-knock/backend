package com.shinhan.knockknock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinhan.knockknock.domain.dto.conversationroom.ChatbotResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationLogResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChainService {

    @Autowired
    ChatbotService chatbotService;

    @Autowired
    PromptService promptService;

    @Autowired
    ConversationLogService conversationLogService;

    public ChatbotResponse chain(ConversationRequest request){
        try {
            // 이전 대화내용 조회
            List<ConversationLogResponse> conversationLogs = conversationLogService.findLast5ByConversationRoomNo(request.getConversationRoomNo());

            // 사용자 입력에 따른 작업 분류
            List<Map<String, String>> classificationPrompt = promptService.classificationPrompt(request.getInput());
            String taskNo = chatbotService.classificationChain(classificationPrompt).trim();

            switch (taskNo){
                case "001" -> System.out.println("1!");
                default -> System.out.println("default");
            }
            // Chatbot 답변 생성
            List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(request.getInput(), conversationLogs);
            ChatbotResponse response = chatbotService.chatbotChain(chatbotPrompt);

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
