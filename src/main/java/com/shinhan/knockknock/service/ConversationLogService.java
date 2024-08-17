package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.conversationroom.ConversationLogRequest;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRoomResponse;
import com.shinhan.knockknock.domain.entity.ConversationLogEntity;
import com.shinhan.knockknock.domain.entity.ConversationRoomEntity;

import java.util.List;

public interface ConversationLogService {

    // Create
    Long createConversationLog(ConversationLogRequest request);

    // Read
    List<ConversationRoomResponse> readAllConversationLog();

    // Update
    void updateConversationLog(ConversationLogRequest request);

    // Delete
    void deleteConversation(long conversationLogNo);

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
//    default ConversationRoomResponse entityToDto(ConversationRoomEntity entity) {
//        return ConversationRoomResponse.builder()
//                .conversationNo(entity.getConversationRoomNo())
//                .conversationStartAt(entity.getConversationRoomStartAt())
//                .conversationEndAt(entity.getConversationRoomEndAt())
//                .userNo(entity.getUserNo())
//                .build();
//    }
}
