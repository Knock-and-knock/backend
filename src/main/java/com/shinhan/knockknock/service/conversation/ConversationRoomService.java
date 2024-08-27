package com.shinhan.knockknock.service.conversation;

import com.shinhan.knockknock.domain.dto.conversation.ConversationLogResponse;
import com.shinhan.knockknock.domain.dto.conversation.ConversationRoomUpdateRequest;
import com.shinhan.knockknock.domain.dto.conversation.ConversationRoomResponse;
import com.shinhan.knockknock.domain.entity.ConversationLogEntity;
import com.shinhan.knockknock.domain.entity.ConversationRoomEntity;

import java.util.List;

public interface ConversationRoomService {

    // Create
    Long createConversationRoom(Long userNo);

    // Read
    List<ConversationRoomResponse> readAllConversationRoom();

    List<ConversationLogResponse> readLatestConversationRoom(long userNo, long conversationRoomNo);

    // Update
    void updateConversationRoom(long conversationRoomNo, ConversationRoomUpdateRequest request);

    void updateConversationRoomEndAt(long conversationRoomNo);

    // Delete
    void deleteConversation(long conversationRoomNo);

    // Dto -> Entity
    default ConversationRoomEntity dtoToEntity(long userId) {
        return ConversationRoomEntity.builder()
                .UserNo(userId)
                .build();
    }

    // Entity -> Dto
    default ConversationRoomResponse entityToDto(ConversationRoomEntity entity) {
        return ConversationRoomResponse.builder()
                .conversationNo(entity.getConversationRoomNo())
                .conversationStartAt(entity.getConversationRoomStartAt())
                .conversationEndAt(entity.getConversationRoomEndAt())
                .userNo(entity.getUserNo())

                .build();
    }
}
