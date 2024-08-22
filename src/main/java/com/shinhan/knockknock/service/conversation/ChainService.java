package com.shinhan.knockknock.service.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinhan.knockknock.domain.dto.conversationroom.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "Unknown User";

        try {
            // 이전 대화내용 조회
            List<ConversationLogResponse> conversationLogs = conversationLogService.findLastNByConversationRoomNo(5, request.getConversationRoomNo());

            // 사용자 입력에 따른 작업 분류
            List<Map<String, String>> classificationPrompt = promptService.classificationPrompt(input, conversationLogs);

            ClassificationResponse classificationResult = chatbotService.classificationChain(classificationPrompt);
            String mainTaskNo = classificationResult.getMainTaskNumber();
            String subTaskNo = classificationResult.getSubTaskNumber();
            log.info("🔗1️⃣ [{}] Task Classification Completed by - Main Task No: {}, Sub Task No: {}", username, mainTaskNo, subTaskNo);

            // Main Task 분류
            ChatbotResponse response;
            switch (mainTaskNo) {
                // 복지 서비스
                case "001" -> {
                    response = welfareService(subTaskNo, input, conversationLogs, username);
                }
                // 금융 서비스
                case "002" -> {
                    return null;
                }
                default -> {
                    response =  dailyConversation(input, conversationLogs);
                }
            }

            log.info("🔗2️⃣ [{}] Response generated for: {}", username, response.getContent());

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ChatbotResponse dailyConversation(String input, List<ConversationLogResponse> conversationLogs) throws JsonProcessingException {
        List<String> promptFilePathList = Collections.singletonList("prompts/basic.prompt");
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);
        return chatbotService.chatbotChain(chatbotPrompt);
    }

    private ChatbotResponse welfareService(String subTaskNo, String input, List<ConversationLogResponse> conversationLogs, String username) throws JsonProcessingException {
        // Sub Task 분류
        InstructionResponse instructionResult = null;
        switch (subTaskNo) {
            case "001-02" -> {
                List<Map<String, String>> instructionPrompt = promptService.instructionPrompt(input, conversationLogs);
                instructionResult = chatbotService.instructionChain(instructionPrompt);
                log.info("🔗3️⃣ [{}] Instruction Chain Completed - Service Number: {}, Action Required: {}", username, instructionResult.getServiceNumber(), instructionResult.getActionRequired());

            }
        }

        // 답변 생성
        List<String> promptFilePathList = Arrays.asList("prompts/basic.prompt", "prompts/welfare.prompt");
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);
        ChatbotResponse response = chatbotService.chatbotChain(chatbotPrompt);

        // 추가 정보 입력
        if (instructionResult != null){
            response.setActionRequired(instructionResult.getActionRequired());
            response.setServiceNumber(instructionResult.getServiceNumber());
        }

        return response;
    }
}
