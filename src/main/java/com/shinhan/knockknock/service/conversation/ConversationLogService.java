package com.shinhan.knockknock.service.conversation;

import com.shinhan.knockknock.domain.dto.conversation.ConversationLogRequest;
import com.shinhan.knockknock.domain.dto.conversation.ConversationLogResponse;
import com.shinhan.knockknock.domain.entity.ConversationLogEntity;
import com.shinhan.knockknock.domain.entity.ConversationRoomEntity;

import java.util.List;

public interface ConversationLogService {

    // Create
    Long createConversationLog(ConversationLogRequest request);

    // Read
    List<ConversationLogResponse> readAllConversationLog();

    List<ConversationLogResponse> findLast5ByConversationRoomNo(long conversationRoomNo);

    List<ConversationLogResponse> findLastNByConversationRoomNo(int number, long conversationRoomNo);

    // Update
    void updateConversationLog(long conversationLogNo, ConversationLogRequest request);

    // Delete
    void deleteConversationLog(long conversationLogNo);

    // Dto -> Entity
    default ConversationLogEntity dtoToEntity(ConversationLogRequest request, ConversationRoomEntity conversationRoom) {
        return ConversationLogEntity.builder()
                .conversationLogInput(request.getConversationLogInput())
                .conversationLogResponse(request.getConversationLogResponse())
                .conversationLogToken(request.getConversationLogToken())
                .conversationRoom(conversationRoom)
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
                .conversationRoomNo(entity.getConversationRoom().getConversationRoomNo())
                .build();
    }
}
