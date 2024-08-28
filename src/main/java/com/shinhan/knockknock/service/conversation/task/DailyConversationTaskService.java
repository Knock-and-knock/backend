package com.shinhan.knockknock.service.conversation.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinhan.knockknock.domain.dto.conversation.ChatbotResponse;
import com.shinhan.knockknock.domain.dto.conversation.ConversationLogResponse;
import com.shinhan.knockknock.service.conversation.ChainService;
import com.shinhan.knockknock.service.conversation.PromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DailyConversationTaskService {

    private final PromptService promptService;
    private final ChainService chainService;

    public ChatbotResponse generateDailyConversation(String input, List<ConversationLogResponse> conversationLogs) throws JsonProcessingException {
        List<String> promptFilePathList = Collections.singletonList("prompts/basic.prompt");
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);
        return chainService.chatbotChain(chatbotPrompt);
    }
}
