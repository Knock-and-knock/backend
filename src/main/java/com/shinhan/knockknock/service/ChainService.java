package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.conversationroom.ChatbotResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationLogResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChainService {

    @Autowired
    ChatbotService chatbotService;

    @Autowired
    ConversationLogService conversationLogService;

    public ChatbotResponse chain(ConversationRequest request){
        chatbotService.classificationChain(request.getInput());

        // 이전 대화내용 조회
        List<ConversationLogResponse> conversationLogs = conversationLogService.findLast5ByConversationRoomNo(request.getConversationRoomNo());

        // Chatbot 답변 생성
        ChatbotResponse response = chatbotService.chatbotChain(request, conversationLogs);
        return response;
    }
}
