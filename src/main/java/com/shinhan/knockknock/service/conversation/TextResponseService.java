package com.shinhan.knockknock.service.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinhan.knockknock.domain.dto.conversation.*;
import com.shinhan.knockknock.domain.dto.user.ReadUserResponse;
import com.shinhan.knockknock.domain.dto.welfarebook.ReadWelfareBookResponse;
import com.shinhan.knockknock.service.welfarebook.WelfareBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextResponseService {

    private final ChainService chainService;

    private final PromptService promptService;

    private final ConversationLogService conversationLogService;

    private final WelfareBookService welfareBookService;

    private static final ModelMapper modelMapper = new ModelMapper();

    public ChatbotResponse TextResponse(ConversationRequest request, ReadUserResponse user) {
        String input = request.getInput();

        try {
            // 이전 대화내용 조회
            List<ConversationLogResponse> conversationLogs = conversationLogService.findLastNByConversationRoomNo(5, request.getConversationRoomNo());

            // 사용자 입력에 따른 작업 분류
            List<Map<String, String>> classificationPrompt = promptService.classificationPrompt(input, conversationLogs);

            ClassificationResponse classificationResult = chainService.classificationChain(classificationPrompt);
            String mainTaskNo = classificationResult.getMainTaskNumber();
            String subTaskNo = classificationResult.getSubTaskNumber();
            log.info("🔗1️⃣ [{}] Task Classification Completed by - Main Task No: {}, Sub Task No: {}", user.getUserId(), mainTaskNo, subTaskNo);

            // Main Task 분류
            ChatbotResponse response;
            switch (mainTaskNo) {
                // 복지 서비스
                case "001" -> {
                    response = welfareService(subTaskNo, input, conversationLogs, user);
                }
                // 금융 서비스
                case "002" -> {
                    return null;
                }
                default -> {
                    response = dailyConversation(input, conversationLogs);
                }
            }

            log.info("🔗2️⃣ [{}] Response generated for: {}", user.getUserId(), response.getContent());

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ChatbotResponse dailyConversation(String input, List<ConversationLogResponse> conversationLogs) throws JsonProcessingException {
        List<String> promptFilePathList = Collections.singletonList("prompts/basic.prompt");
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);
        return chainService.chatbotChain(chatbotPrompt);
    }

    private ChatbotResponse welfareService(String subTaskNo, String input, List<ConversationLogResponse> conversationLogs, ReadUserResponse user) throws JsonProcessingException {
        // Chatbot Prompt 제작
        List<String> promptFilePathList = Arrays.asList("prompts/basic.prompt", "prompts/welfare.prompt");
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);

        // Sub Task 분류
        RedirectionResponse redirectionResult = null;
        ReservationResponse reservationResult = null;
        switch (subTaskNo) {
            case "001-02" -> {
                List<Map<String, String>> redirectionPrompt = promptService.redirectionPrompt(input, conversationLogs);
                redirectionResult = chainService.redirectionChain(redirectionPrompt);
                log.info("🔗3️⃣ [{}] Instruction Chain Completed - Service Number: {}, Action Required: {}", user.getUserId(), redirectionResult.getServiceNumber(), redirectionResult.isActionRequired());
            }
            case "001-03" -> {
                List<Map<String, String>> reservationPrompt = promptService.reservationPrompt(input, conversationLogs);
                reservationResult = chainService.reservationChain(reservationPrompt);
            }
            case "001-04" -> {
                List<ReadWelfareBookResponse> welfareBookList = welfareBookService.readAllByLastMonth(user.getUserNo());
                List<WelfareBookInfoDto> bookList = welfareBookList.stream()
                        .map(source -> modelMapper.map(source, WelfareBookInfoDto.class))
                        .toList();

                // chatbotPrompt에 추가 정보로 bookList 문자열을 넣음
                String bookListString = "\nAdditional Info:\n" + bookList.stream()
                        .map(WelfareBookInfoDto::toString)  // 각 DTO 객체를 문자열로 변환
                        .collect(Collectors.joining("\n"));

                chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs, bookListString);
            }
        }
        System.out.println("====================================================");
        System.out.println(chatbotPrompt);
        System.out.println("====================================================");
        // 답변 생성
        ChatbotResponse response = chainService.chatbotChain(chatbotPrompt);

        // 추가 정보 입력
        if (redirectionResult != null) {
            response.setActionRequired(redirectionResult.isActionRequired());
            response.setRedirectionResult(redirectionResult);
        }
        if (reservationResult != null) {
            response.setActionRequired(reservationResult.isActionRequired());
            response.setReservationResult(reservationResult);
        }

        return response;
    }
}
