package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.entity.ConversationRoomEntity;

public interface ConversationRoomService {

    // Create
    Long createConversationRoom(Long userNo);

    default ConversationRoomEntity dtoToEntity(long userId) {
        ConversationRoomEntity entity = ConversationRoomEntity.builder()
                .UserNo(userId)
                .build();
        return entity;
    }
}
