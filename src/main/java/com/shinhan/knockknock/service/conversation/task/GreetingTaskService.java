package com.shinhan.knockknock.service.conversation.task;

import com.shinhan.knockknock.domain.dto.conversation.ChatbotResponse;
import com.shinhan.knockknock.domain.dto.conversation.ConversationLogResponse;
import com.shinhan.knockknock.domain.dto.conversation.ConversationRequest;
import com.shinhan.knockknock.service.conversation.ChainService;
import com.shinhan.knockknock.service.conversation.ConversationRoomService;
import com.shinhan.knockknock.service.conversation.PromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GreetingTaskService {

    private final ConversationRoomService conversationRoomService;
    private final PromptService promptService;
    private final ChainService chainService;

    public ChatbotResponse generateGreeting(ConversationRequest request, long userNo) {
        List<ConversationLogResponse> conversationLogList = conversationRoomService.readLatestConversationRoom(userNo, request.getConversationRoomNo());
        String conversationLogListString = conversationLogList != null ? "\nPrevious Conversation:\n" + conversationLogList.stream()
                .map(ConversationLogResponse::toLogString)
                .collect(Collectors.joining("\n")) : "";

        List<String> promptFilePathList = Arrays.asList("prompts/basic.prompt", "prompts/greeting.prompt");
        List<ConversationLogResponse> conversationLogs = Collections.emptyList();
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, "", conversationLogs, conversationLogListString);
        return chainService.chatbotChain(chatbotPrompt);
    }
}
