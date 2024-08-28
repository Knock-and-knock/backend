package com.shinhan.knockknock.service.conversation;

import com.shinhan.knockknock.domain.dto.conversation.ConversationLogResponse;
import com.shinhan.knockknock.domain.dto.conversation.ConversationRoomUpdateRequest;
import com.shinhan.knockknock.domain.dto.conversation.ConversationRoomResponse;
import com.shinhan.knockknock.domain.entity.ConversationRoomEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;

import java.sql.Timestamp;
import java.util.List;

public interface ConversationRoomService {

    // Create
    Long createConversationRoom(Long userNo);

    // Read
    List<ConversationRoomResponse> readAllConversationRoom();

    void readConversationRoomByConversationRoomNo(long conversationRoomNo);

    Timestamp readLastConversationTime(long userNo);

    List<ConversationLogResponse> readLatestConversationRoom(long userNo, long conversationRoomNo);

    // Update
    void updateConversationRoom(long conversationRoomNo, ConversationRoomUpdateRequest request);

    void updateConversationRoomEndAt(long conversationRoomNo);

    // Delete
    void deleteConversation(long conversationRoomNo);

    // Dto -> Entity
    default ConversationRoomEntity dtoToEntity(UserEntity user) {
        return ConversationRoomEntity.builder()
                .user(user)
                .build();
    }

    // Entity -> Dto
    default ConversationRoomResponse entityToDto(ConversationRoomEntity entity) {
        return ConversationRoomResponse.builder()
                .conversationNo(entity.getConversationRoomNo())
                .conversationStartAt(entity.getConversationRoomStartAt())
                .conversationEndAt(entity.getConversationRoomEndAt())
                .userNo(entity.getUser().getUserNo())
                .build();
    }
}
