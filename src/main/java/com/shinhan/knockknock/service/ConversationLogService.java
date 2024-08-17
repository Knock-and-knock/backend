package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.conversationroom.ConversationLogRequest;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationLogResponse;
import com.shinhan.knockknock.domain.entity.ConversationLogEntity;

import java.util.List;

public interface ConversationLogService {

    // Create
    Long createConversationLog(ConversationLogRequest request);

    // Read
    List<ConversationLogResponse> readAllConversationLog();

    // Update
    void updateConversationLog(long conversationLogNo, ConversationLogRequest request);

    // Delete
    void deleteConversationLog(long conversationLogNo);

    // Dto -> Entity
    default ConversationLogEntity dtoToEntity(ConversationLogRequest request) {
        return ConversationLogEntity.builder()
                .conversationLogInput(request.getConversationLogInput())
                .conversationLogResponse(request.getConversationLogResponse())
                .conversationLogToken(request.getConversationLogToken())
                .conversationRoomNo(request.getConversationRoomNo())
                .build();
    }

    // Entity -> Dto
    default ConversationLogResponse entityToDto(ConversationLogEntity entity) {
        return ConversationLogResponse.builder()
                .conversationLogNo(entity.getConversationLogNo())
                .conversationLogInput(entity.getConversationLogInput())
                .conversationLogResponse(entity.getConversationLogResponse())
                .conversationLogToken(entity.getConversationLogToken())
                .conversationLogDatetime(entity.getConversationLogDatetime())
                .conversationRoomNo(entity.getConversationRoomNo())
                .build();
    }
}
