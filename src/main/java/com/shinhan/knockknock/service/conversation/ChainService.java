package com.shinhan.knockknock.service.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinhan.knockknock.domain.dto.conversationroom.*;
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
            List<ConversationLogResponse> conversationLogs = conversationLogService.findLastNByConversationRoomNo(5, request.getConversationRoomNo());

            // 사용자 입력에 따른 작업 분류
            List<Map<String, String>> classificationPrompt = promptService.classificationPrompt(input, conversationLogs);

            ClassificationResponse classificationResult = chatbotService.classificationChain(classificationPrompt);
            String mainTaskNo = classificationResult.getMainTaskNumber();
            String subTaskNo = classificationResult.getSubTaskNumber();

            System.out.println("mainTaskNo: " + mainTaskNo);
            System.out.println("subTaskNo: " + subTaskNo);

            // Prompt 제작
            switch (mainTaskNo) {
                case "001" -> {
                    return welfareService(subTaskNo, input, conversationLogs);
                }
                default -> {
                    return dailyConversation(input, conversationLogs);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ChatbotResponse dailyConversation(String input, List<ConversationLogResponse> conversationLogs) {
        List<String> promptFilePathList = Collections.singletonList("prompts/basic.prompt");
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);
        return chatbotService.chatbotChain(chatbotPrompt);
    }

    private ChatbotResponse welfareService(String subTaskNo, String input, List<ConversationLogResponse> conversationLogs) throws JsonProcessingException {
        InstructionResponse instructionResult = null;
        switch (subTaskNo) {
            case "001-02" -> {
                List<Map<String, String>> instructionPrompt = promptService.instructionPrompt(input, conversationLogs);
                instructionResult = chatbotService.instructionChain(instructionPrompt);
            }
        }
        List<String> promptFilePathList = Arrays.asList("prompts/basic.prompt", "prompts/welfare.prompt");
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);
        ChatbotResponse response = chatbotService.chatbotChain(chatbotPrompt);

        if (instructionResult != null){
            response.setActionRequired(instructionResult.getActionRequired());
            response.setServiceNumber(instructionResult.getServiceNumber());
        }

        return response;
    }
}
