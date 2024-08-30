package com.shinhan.knockknock.service.conversation.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinhan.knockknock.domain.dto.consumption.ReadConsumptionResponse;
import com.shinhan.knockknock.domain.dto.conversation.ChatbotResponse;
import com.shinhan.knockknock.domain.dto.conversation.ConsumptionResponse;
import com.shinhan.knockknock.domain.dto.conversation.ConversationLogResponse;
import com.shinhan.knockknock.domain.dto.conversation.ReservationResponse;
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
        ConsumptionResponse consumptionReportResult = null;

        // Chatbot Prompt 제작
        List<String> promptFilePathList = Arrays.asList("prompts/basic.prompt", "prompts/finance.prompt");
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);

        switch (subTaskNo) {
            // 카드 사용 내역 읽어주기
            case "002-01" -> {

            }
            // 소비 리포트 확인
            case "002-02" -> {
                List<Map<String, String>> consumptionReportPrompt = promptService.consumptionReportPrompt(input, conversationLogs);
                consumptionReportResult = chainService.consumptionReportChain(consumptionReportPrompt);
                int year = consumptionReportResult.getYear();
                int month = consumptionReportResult.getMonth();
                String dateString = year + "-" + String.format("%02d", month);

                log.info("🔗3️⃣ [{}] Processing consumption report for date: \u001B[32m{}\u001B[0m", user.getUserId(), dateString);

                if (consumptionReportResult.getYear() == 0 || consumptionReportResult.getMonth() == 0) {
                    // 특정 년,월이 지정되지 않은 경우
                    String message = "Year or month information is missing in the consumption report.";
                    chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs, message);
                } else {
                    // 특정 년,월이 지정된 경우

                    CardEntity card = cardHistoryService.readTopUsedCardLastMonth(user.getUserNo());
                    String report = consumptionService.readConsumptionReportForConversation(card, dateString);
                    chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs, report);
                }
            }
        }

        // 답변 생성
        ChatbotResponse response = chainService.chatbotChain(chatbotPrompt);

        // 추가 정보 입력
        if (consumptionReportResult != null) {
            tokenService.calculateToken(response, consumptionReportResult);
        }

        return response;
    }
}
