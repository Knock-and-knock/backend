package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.entity.ConversationRoomEntity;
import com.shinhan.knockknock.repository.ConversationRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversationRoomServiceImpl implements ConversationRoomService {

    @Autowired
    ConversationRoomRepository conversationRoomRepository;

    @Override
    public Long createConversationRoom(Long userNo) {
        ConversationRoomEntity newConversationRoom = conversationRoomRepository.save(dtoToEntity(userNo));
        return 0L;
    }
}
