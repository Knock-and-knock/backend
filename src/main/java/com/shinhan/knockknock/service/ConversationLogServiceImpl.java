package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.conversationroom.ConversationLogRequest;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRoomResponse;
import com.shinhan.knockknock.domain.entity.ConversationLogEntity;

import java.util.List;

public class ConversationLogServiceImpl implements ConversationLogService {

    @Override
    public Long createConversationLog(ConversationLogRequest request) {
        return 0L;
    }

    @Override
    public List<ConversationRoomResponse> readAllConversationLog() {
        return List.of();
    }

    @Override
    public void updateConversationLog(ConversationLogRequest request) {

    }

    @Override
    public void deleteConversation(long conversationLogNo) {

    }
}
