package com.shinhan.knockknock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinhan.knockknock.domain.dto.conversationroom.ChatbotResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationLogResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
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
        String input = request.getInput();

        try {
            // 이전 대화내용 조회
            List<ConversationLogResponse> conversationLogs = conversationLogService.findLast5ByConversationRoomNo(request.getConversationRoomNo());

            // 사용자 입력에 따른 작업 분류
            List<Map<String, String>> classificationPrompt = promptService.classificationPrompt(request.getInput());
            String taskNo = chatbotService.classificationChain(classificationPrompt).trim();

            // Prompt 제작
            List<Map<String, String>> chatbotPrompt;
            switch (taskNo){
                case "001" -> {
                    List<String> promptFilePathList = Arrays.asList("prompts/basic.prompt", "prompts/welfare.prompt");
                    chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);
                    System.out.println("1111111111111111111111");
                    System.out.println(chatbotPrompt);
                }
                default -> {
                    List<String> promptFilePathList = Collections.singletonList("prompts/basic.prompt");
                    chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);
                    System.out.println(chatbotPrompt);
                }
            }

            // Chatbot 답변 생성
            ChatbotResponse response = chatbotService.chatbotChain(chatbotPrompt);

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
