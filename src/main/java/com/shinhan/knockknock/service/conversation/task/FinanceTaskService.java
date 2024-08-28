package com.shinhan.knockknock.service.conversation.task;

import com.shinhan.knockknock.domain.dto.conversation.ChatbotResponse;
import com.shinhan.knockknock.domain.dto.conversation.ConversationLogResponse;
import com.shinhan.knockknock.domain.dto.user.ReadUserResponse;
import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.service.consumption.ConsumptionService;
import com.shinhan.knockknock.service.conversation.ChainService;
import com.shinhan.knockknock.service.conversation.PromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FinanceTaskService {

    private final PromptService promptService;
    private final ChainService chainService;
    private final ConsumptionService consumptionService;

    public ChatbotResponse generateFinancialService(String subTaskNo, String input, List<ConversationLogResponse> conversationLogs, ReadUserResponse user) {
        // Chatbot Prompt 제작
        List<String> promptFilePathList = Arrays.asList("prompts/basic.prompt", "prompts/finance.prompt");
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);

        switch (subTaskNo) {
            // 카드 사용 내역 읽어주기
            case "002-01" -> {

            }
            // 소비 리포트 확인
            case "002-02" -> {
                List<CardEntity> cardList = consumptionService.readCardByUserNo(user.getUserNo());
                for (CardEntity card:cardList){
                    System.out.println(card.getCardNo());
                }
//                consumptionService.readConsumptionReportForConversation();
            }
        }

        // 답변 생성
        ChatbotResponse response = chainService.chatbotChain(chatbotPrompt);
        System.out.println("===============================================================");
        System.out.println(chatbotPrompt);
        System.out.println("===============================================================");
        System.out.println("===============================================================");
        System.out.println(response);
        System.out.println("===============================================================");

        return response;
    }
}
