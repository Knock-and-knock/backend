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

    private final ConversationRoomService conversationRoomService;

    private final ConversationLogService conversationLogService;

    private final WelfareBookService welfareBookService;

    private static final ModelMapper modelMapper = new ModelMapper();

    public ChatbotResponse TextResponse(ConversationRequest request, ReadUserResponse user) throws JsonProcessingException {
        String input = request.getInput();

        // 이전 대화내용 조회
        List<ConversationLogResponse> conversationLogs = conversationLogService.findLastNByConversationRoomNo(5, request.getConversationRoomNo());

        if (conversationLogs.isEmpty() && request.getInput().equals("Greeting")) {
            generateGreeting(user.getUserNo(), request.getConversationRoomNo());
        }

        // 사용자 입력에 따른 작업 분류
        List<Map<String, String>> classificationPrompt = promptService.classificationPrompt(input, conversationLogs);

        ClassificationResponse classificationResult = chainService.classificationChain(classificationPrompt);
        String mainTaskNo = classificationResult.getMainTaskNumber();
        String subTaskNo = classificationResult.getSubTaskNumber();
        log.info("🔗1️⃣ [{}] Task Classification Completed by - Main Task No: \u001B[34m{}\u001B[0m, Sub Task No: \u001B[34m{}\u001B[0m", user.getUserId(), mainTaskNo, subTaskNo);


        // Main Task 분류
        ChatbotResponse response;
        switch (mainTaskNo) {
            // 복지 서비스
            case "001" -> {
                response = generateWelfareService(subTaskNo, input, conversationLogs, user);
            }
            // 금융 서비스
            case "002" -> {
                return null;
            }
            default -> {
                response = generateDailyConversation(input, conversationLogs);
            }
        }

        // 전체 token 계산
        calculateToken(response, classificationResult);

        log.info("🔗2️⃣ [{}] Response generated for: {}", user.getUserId(), response.getContent());
        return response;

    }

    private void generateGreeting(long userNo, long conversationRoomNo) {
        List<ConversationLogResponse> conversationLogList = conversationRoomService.readLatestConversationRoom(userNo, conversationRoomNo);

        System.out.println(conversationLogList);
    }

    private ChatbotResponse generateDailyConversation(String input, List<ConversationLogResponse> conversationLogs) throws JsonProcessingException {
        List<String> promptFilePathList = Collections.singletonList("prompts/basic.prompt");
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);
        return chainService.chatbotChain(chatbotPrompt);
    }

    private ChatbotResponse generateWelfareService(String subTaskNo, String input, List<ConversationLogResponse> conversationLogs, ReadUserResponse user) throws JsonProcessingException {
        RedirectionResponse redirectionResult = null;
        ReservationResponse reservationResult = null;

        // Chatbot Prompt 제작
        List<String> promptFilePathList = Arrays.asList("prompts/basic.prompt", "prompts/welfare.prompt");
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);

        // Sub Task 분류
        switch (subTaskNo) {
            // 복지 서비스 바로가기
            case "001-02" -> {
                List<Map<String, String>> redirectionPrompt = promptService.redirectionPrompt(input, conversationLogs);
                redirectionResult = chainService.redirectionChain(redirectionPrompt);
                log.info("🔗3️⃣ [{}] Redirection Chain Completed - Action Required: \u001B[32m{}\u001B[0m, Service Number: \u001B[34m{}\u001B[0m", user.getUserId(), redirectionResult.isActionRequired(), redirectionResult.getServiceName());
            }
            // 돌봄 서비스 예약하기
            case "001-03" -> {
                List<Map<String, String>> reservationPrompt = promptService.reservationPrompt(input, conversationLogs);
                reservationResult = chainService.reservationChain(reservationPrompt);
                log.info("🔗3️⃣ [{}] Reservation Chain Completed - Action Required: \u001B[32m{}\u001B[0m, Service Type Number: \u001B[34m{}\u001B[0m, Reservation Date: {}, Reservation Time Number: {}",
                        user.getUserId(),
                        reservationResult.isActionRequired(),
                        reservationResult.getServiceTypeNumber(),
                        reservationResult.getReservationDate(),
                        reservationResult.getReservationTimeNumber());
            }
            // 돌봄 서비스 예약 내역 확인
            case "001-04" -> {
                List<ReadWelfareBookResponse> welfareBookList = welfareBookService.readAllByLastMonth(user.getUserNo());
                List<WelfareBookInfoDto> bookList = welfareBookList.stream()
                        .map(source -> modelMapper.map(source, WelfareBookInfoDto.class))
                        .toList();

                String bookListString = "\nAdditional Info:\n" + bookList.stream()
                        .map(WelfareBookInfoDto::toString)  // 각 DTO 객체를 문자열로 변환
                        .collect(Collectors.joining("\n"));

                chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs, bookListString);
            }
        }

        // 답변 생성
        ChatbotResponse response = chainService.chatbotChain(chatbotPrompt);

        // 추가 정보 입력
        if (redirectionResult != null) {
            response.setActionRequired(redirectionResult.isActionRequired());
            response.setRedirectionResult(redirectionResult);
            calculateToken(response, redirectionResult);
        }
        if (reservationResult != null) {
            response.setActionRequired(reservationResult.isActionRequired());
            response.setReservationResult(reservationResult);
            calculateToken(response, reservationResult);
        }

        return response;
    }

    private void calculateToken(ChatbotResponse response, ClassificationResponse classificationResult) {
        log.debug("🪙 Classification Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", classificationResult.getPromptTokens(), classificationResult.getCompletionTokens(), classificationResult.getTotalTokens());
        log.debug("🪙 Chatbot Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", response.getPromptTokens(), response.getCompletionTokens(), response.getTotalTokens());

        response.setPromptTokens(response.getPromptTokens() + classificationResult.getPromptTokens());
        response.setCompletionTokens(response.getCompletionTokens() + classificationResult.getCompletionTokens());
        response.setTotalTokens(response.getTotalTokens() + classificationResult.getTotalTokens());
    }

    private void calculateToken(ChatbotResponse response, RedirectionResponse redirectionResult) {
        log.debug("🪙 Redirection Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", redirectionResult.getPromptTokens(), redirectionResult.getCompletionTokens(), redirectionResult.getTotalTokens());
        log.debug("🪙 Chatbot Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", response.getPromptTokens(), response.getCompletionTokens(), response.getTotalTokens());

        response.setPromptTokens(response.getPromptTokens() + redirectionResult.getPromptTokens());
        response.setCompletionTokens(response.getCompletionTokens() + redirectionResult.getCompletionTokens());
        response.setTotalTokens(response.getTotalTokens() + redirectionResult.getTotalTokens());
    }

    private void calculateToken(ChatbotResponse response, ReservationResponse reservationResult) {
        log.debug("🪙 Reservation Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", reservationResult.getPromptTokens(), reservationResult.getCompletionTokens(), reservationResult.getTotalTokens());
        log.debug("🪙 Chatbot Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", response.getPromptTokens(), response.getCompletionTokens(), response.getTotalTokens());

        response.setPromptTokens(response.getPromptTokens() + reservationResult.getPromptTokens());
        response.setCompletionTokens(response.getCompletionTokens() + reservationResult.getCompletionTokens());
        response.setTotalTokens(response.getTotalTokens() + reservationResult.getTotalTokens());
    }
}
