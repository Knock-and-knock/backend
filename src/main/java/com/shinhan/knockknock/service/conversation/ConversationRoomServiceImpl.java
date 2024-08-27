package com.shinhan.knockknock.service.conversation;

import com.shinhan.knockknock.domain.dto.conversation.ConversationLogResponse;
import com.shinhan.knockknock.domain.dto.conversation.ConversationRoomUpdateRequest;
import com.shinhan.knockknock.domain.dto.conversation.ConversationRoomResponse;
import com.shinhan.knockknock.domain.entity.ConversationLogEntity;
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

    @Autowired
    ConversationLogService conversationLogService;

    @Override
    public Long createConversationRoom(Long userNo) {
        ConversationRoomEntity newConversationRoom = conversationRoomRepository.save(dtoToEntity(userNo));
        return newConversationRoom.getConversationRoomNo();
    }

    @Override
    public List<ConversationRoomResponse> readAllConversationRoom() {
        return conversationRoomRepository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConversationLogResponse> readLatestConversationRoom(long userNo, long conversationRoomNo) {
        ConversationRoomEntity conversationRoom = conversationRoomRepository.findLatestByUserNoExcludingConversationRoomNo(userNo, conversationRoomNo);

        List<ConversationLogEntity> conversationLogs = conversationRoom.getConversationLogs();

        // 마지막 5개 가져오기
        int size = conversationLogs.size();
        List<ConversationLogEntity> lastThreeLogs = conversationLogs.subList(Math.max(size - 5, 0), size);

        return lastThreeLogs.stream()
                .map(logEntity -> conversationLogService.entityToDto(logEntity))
                .collect(Collectors.toList());
    }

    @Override
    public void updateConversationRoom(long conversationRoomNo, ConversationRoomUpdateRequest request) {
        conversationRoomRepository.findById(conversationRoomNo)
                .ifPresent(conversationRoom -> {
                    LocalDateTime endAt = request.getConversationEndAt();
                    conversationRoom.setConversationRoomEndAt(endAt != null ? Timestamp.valueOf(endAt) : null);

                    conversationRoomRepository.save(conversationRoom);
                });
    }

    @Override
    public void updateConversationRoomEndAt(long conversationRoomNo) {
        conversationRoomRepository.findById(conversationRoomNo)
                .ifPresent(conversationRoom -> {
                    conversationRoom.setConversationRoomEndAt(Timestamp.valueOf(LocalDateTime.now()));
                    conversationRoomRepository.save(conversationRoom);
                });
    }

    @Override
    public void deleteConversation(long conversationRoomNo) {
        conversationRoomRepository
                .findById(conversationRoomNo)
                .ifPresent(conversationRoomRepository::delete);
    }
}
