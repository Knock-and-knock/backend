package com.shinhan.knockknock.service.conversation.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinhan.knockknock.domain.dto.consumption.ReadConsumptionResponse;
import com.shinhan.knockknock.domain.dto.conversation.*;
import com.shinhan.knockknock.domain.dto.user.ReadUserResponse;
import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.service.cardhistory.CardHistoryService;
import com.shinhan.knockknock.service.consumption.ConsumptionService;
import com.shinhan.knockknock.service.conversation.ChainService;
import com.shinhan.knockknock.service.conversation.PromptService;
import com.shinhan.knockknock.service.conversation.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinanceTaskService {

    private final PromptService promptService;
    private final ChainService chainService;
    private final ConsumptionService consumptionService;
    private final CardHistoryService cardHistoryService;
    private final TokenService tokenService;

    public ChatbotResponse generateFinancialService(String subTaskNo, String input, List<ConversationLogResponse> conversationLogs, ReadUserResponse user) throws JsonProcessingException {
        ConsumptionResponse consumptionResult = null;
        ConsumptionReportResponse consumptionReportResult = null;

        // Chatbot Prompt 제작
        List<String> promptFilePathList = Arrays.asList("prompts/basic.prompt", "prompts/finance.prompt");
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);

        // 사용자의 가장 많이 사용한 카드 조회
        CardEntity card = cardHistoryService.readTopUsedCardLastMonth(user.getUserNo());

        switch (subTaskNo) {
            // 카드 사용 내역 읽어주기
            case "002-01" -> {
                List<Map<String, String>> consumptionPrompt = promptService.consumptionPrompt(input, conversationLogs);
                consumptionResult = chainService.consumptionChain(consumptionPrompt);

                Timestamp[] timestamps = makeTimestamps(consumptionResult);

                String data;
                if (timestamps == null) {
                    // 유효하지 않은 날짜 정보가 있는 경우
                    data = "Consumption data contains invalid date information.";
                } else {
                    // 유효한 날짜 정보가 있는 경우, 필요 시 timestamps를 활용
                    data = cardHistoryService.readAllWithinDateRangeForConversation(card.getCardId(), timestamps[0], timestamps[1]);
                }
                chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs, data);
            }
            // 소비 리포트 확인
            case "002-02" -> {
                List<Map<String, String>> consumptionReportPrompt = promptService.consumptionReportPrompt(input, conversationLogs);
                consumptionReportResult = chainService.consumptionReportChain(consumptionReportPrompt);
                int year = consumptionReportResult.getYear();
                int month = consumptionReportResult.getMonth();
                String dateString = year + "-" + String.format("%02d", month);

                log.info("🔗3️⃣ [{}] Processing consumption report for date: \u001B[32m{}\u001B[0m", user.getUserId(), dateString);

                String data;
                if (consumptionReportResult.getYear() == 0 || consumptionReportResult.getMonth() == 0) {
                    // 특정 년,월이 지정되지 않은 경우
                    data = "Year or month information is missing in the consumption report.";
                } else {
                    // 특정 년,월이 지정된 경우
                    data = "\nReport:\n" + consumptionService.readConsumptionReportForConversation(card, dateString);
                }
                chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs, data);
            }
        }

        System.out.println(chatbotPrompt);

        // 답변 생성
        ChatbotResponse response = chainService.chatbotChain(chatbotPrompt);

        // 추가 정보 입력
        if (consumptionResult != null) {
            tokenService.calculateToken(response, consumptionResult);
        }
        if (consumptionReportResult != null) {
            tokenService.calculateToken(response, consumptionReportResult);
        }

        return response;
    }

    public Timestamp[] makeTimestamps(ConsumptionResponse consumptionResult) {
        // 시작일 정보 추출
        int startYear = consumptionResult.getStartYear();
        int startMonth = consumptionResult.getStartMonth();
        int startDay = consumptionResult.getStartDay();

        // 종료일 정보 추출
        int endYear = consumptionResult.getEndYear();
        int endMonth = consumptionResult.getEndMonth();
        int endDay = consumptionResult.getEndDay();

        // 시작일 유효성 검사
        if (!isValidDate(startYear, startMonth, startDay)) {
            log.warn("Invalid start date: year={}, month={}, day={}", startYear, startMonth, startDay);
            return null;
        }

        // 종료일 유효성 검사
        if (!isValidDate(endYear, endMonth, endDay)) {
            log.warn("Invalid end date: year={}, month={}, day={}", endYear, endMonth, endDay);
            return null;
        }

        try {
            // LocalDateTime 객체 생성
            LocalDateTime startDateTime = LocalDateTime.of(startYear, startMonth, startDay, 0, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(endYear, endMonth, endDay, 23, 59, 59);

            // Timestamp 변환
            Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
            Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

            return new Timestamp[]{startTimestamp, endTimestamp};
        } catch (DateTimeException e) {
            log.error("Error creating LocalDateTime: {}", e.getMessage());
            return null;
        }
    }
    private boolean isValidDate(int year, int month, int day) {
        // 연도 유효성 검사 (예: 1 이상)
        if (year <= 0) {
            log.debug("Invalid year: {}", year);
            return false;
        }

        // 월 유효성 검사
        if (month < 1 || month > 12) {
            log.debug("Invalid month: {}", month);
            return false;
        }

        // 일 유효성 검사
        try {
            YearMonth yearMonth = YearMonth.of(year, month);
            int maxDay = yearMonth.lengthOfMonth();
            if (day < 1 || day > maxDay) {
                log.debug("Invalid day: {}", day);
                return false;
            }
        } catch (DateTimeException e) {
            log.debug("DateTimeException during validation: {}", e.getMessage());
            return false;
        }

        return true;
    }
}
