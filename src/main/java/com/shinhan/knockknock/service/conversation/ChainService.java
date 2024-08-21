package com.shinhan.knockknock.service.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinhan.knockknock.domain.dto.conversationroom.ChatbotResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationLogResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRequest;
import com.shinhan.knockknock.service.WelfareService;
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

    public ChatbotResponse chain(ConversationRequest request) {
        String input = request.getInput();

        try {
            // 이전 대화내용 조회
            List<ConversationLogResponse> conversationLogs = conversationLogService.findLast5ByConversationRoomNo(request.getConversationRoomNo());

            // 사용자 입력에 따른 작업 분류
            List<Map<String, String>> classificationPrompt = promptService.classificationPrompt(request.getInput());
            String mainTaskNo = chatbotService.classificationChain(classificationPrompt).trim();
            System.out.println("mainTaskNo: " + mainTaskNo);

            // Prompt 제작
            List<Map<String, String>> chatbotPrompt;
            switch (mainTaskNo) {
                case "001" -> {
                    chatbotPrompt = welfareService(input, conversationLogs);
                }
                default -> {
                    chatbotPrompt = dailyConversation(input, conversationLogs);
                }
            }

            // Chatbot 답변 생성
            ChatbotResponse response = chatbotService.chatbotChain(chatbotPrompt);

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Map<String, String>> dailyConversation(String input, List<ConversationLogResponse> conversationLogs){
        List<String> promptFilePathList = Collections.singletonList("prompts/basic.prompt");
        return promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);
    }

    private List<Map<String, String>> welfareService(String input, List<ConversationLogResponse> conversationLogs) {
        List<String> promptFilePathList = Arrays.asList("prompts/basic.prompt", "prompts/welfare.prompt");
        return promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);
    }
}
