package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.ConversationRoomRequest;
import com.shinhan.knockknock.domain.dto.ConversationRoomResponse;
import com.shinhan.knockknock.domain.entity.ConversationRoomEntity;
import com.shinhan.knockknock.repository.ConversationRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConversationRoomServiceImpl implements ConversationRoomService {

    @Autowired
    ConversationRoomRepository conversationRoomRepository;

    @Override
    public Long createConversationRoom(Long userNo) {
        ConversationRoomEntity newConversationRoom = conversationRoomRepository.save(dtoToEntity(userNo));
        return 0L;
    }

    @Override
    public List<ConversationRoomResponse> readAllConversationRoom() {
        return conversationRoomRepository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateConversationRoom(ConversationRoomRequest request) {
        conversationRoomRepository.findById(request.getConversationNo())
                .ifPresent(conversationRoom -> {
                    conversationRoom.setConversationRoomNo(request.getConversationNo());

                    LocalDateTime endAt = request.getConversationEndAt();
                    conversationRoom.setConversationRoomStartAt(endAt != null ? Timestamp.valueOf(endAt) : null);
                    conversationRoom.setUserNo(request.getUserNo());

                    conversationRoomRepository.save(conversationRoom);
                });
    }
}
