package com.shinhan.knockknock.service.conversation.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinhan.knockknock.domain.dto.conversation.*;
import com.shinhan.knockknock.domain.dto.user.ReadUserResponse;
import com.shinhan.knockknock.domain.dto.welfarebook.ReadWelfareBookResponse;
import com.shinhan.knockknock.service.conversation.ChainService;
import com.shinhan.knockknock.service.conversation.PromptService;
import com.shinhan.knockknock.service.conversation.TokenService;
import com.shinhan.knockknock.service.welfarebook.WelfareBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class WelfareTaskService {

    private final PromptService promptService;
    private final ChainService chainService;
    private final WelfareBookService welfareBookService;
    private final TokenService tokenService;

    private static final ModelMapper modelMapper = new ModelMapper();

    public ChatbotResponse generateWelfareService(String subTaskNo, String input, List<ConversationLogResponse> conversationLogs, ReadUserResponse user) throws JsonProcessingException {
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

        // 추가 정보 입력 & 페이지 이동 검증
        validateActionRequired(response, redirectionResult, reservationResult);

        return response;
    }

    private void validateActionRequired(ChatbotResponse response, RedirectionResponse redirectionResult, ReservationResponse reservationResult) {
        if (redirectionResult != null) {
            response.setActionRequired(redirectionResult.isActionRequired());
            response.setRedirectionResult(redirectionResult);
            tokenService.calculateToken(response, redirectionResult);
        }

        if (reservationResult != null) {
            boolean isValidDate = isDateValid(reservationResult.getReservationDate());
            boolean newActionRequired = reservationResult.isActionRequired() && isValidDate && reservationResult.getReservationTimeNumber() != 0 && reservationResult.getServiceTypeNumber() != 0;

            response.setActionRequired(newActionRequired);
            response.setReservationResult(reservationResult);
            tokenService.calculateToken(response, reservationResult);
        }
    }

    private boolean isDateValid(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
