package com.shinhan.knockknock.service.conversation.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinhan.knockknock.domain.dto.conversation.ClassificationResponse;
import com.shinhan.knockknock.domain.dto.conversation.ConversationLogResponse;
import com.shinhan.knockknock.service.conversation.ChainService;
import com.shinhan.knockknock.service.conversation.PromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClassificationTaskService {

    private final PromptService promptService;
    private final ChainService chainService;

    public ClassificationResponse classificationTask(String input, List<ConversationLogResponse> conversationLogs) throws JsonProcessingException {
        List<Map<String, String>> classificationPrompt = promptService.classificationPrompt(input, conversationLogs);
        return chainService.classificationChain(classificationPrompt);
    }
}
