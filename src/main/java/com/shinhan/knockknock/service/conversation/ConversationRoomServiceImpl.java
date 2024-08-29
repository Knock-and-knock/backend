package com.shinhan.knockknock.service.conversation;

import com.shinhan.knockknock.domain.dto.conversation.ConversationLogResponse;
import com.shinhan.knockknock.domain.dto.conversation.ConversationRoomUpdateRequest;
import com.shinhan.knockknock.domain.dto.conversation.ConversationRoomResponse;
import com.shinhan.knockknock.domain.entity.ConversationLogEntity;
import com.shinhan.knockknock.domain.entity.ConversationRoomEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.exception.ConversationRoomNotFoundException;
import com.shinhan.knockknock.repository.ConversationRoomRepository;
import com.shinhan.knockknock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationRoomServiceImpl implements ConversationRoomService {

    private final ConversationRoomRepository conversationRoomRepository;

    private final ConversationLogService conversationLogService;
    private final UserRepository userRepository;

    @Override
    public Long createConversationRoom(Long userNo) {
        UserEntity user = userRepository.findById(userNo).orElse(null);
        ConversationRoomEntity newConversationRoom = conversationRoomRepository.save(dtoToEntity(user));
        return newConversationRoom.getConversationRoomNo();
    }

    @Override
    public List<ConversationRoomResponse> readAllConversationRoom() {
        return conversationRoomRepository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void readConversationRoomByConversationRoomNo(long conversationRoomNo) {
        ConversationRoomEntity room = conversationRoomRepository.findById(conversationRoomNo).orElse(null);
        if (room == null) {
            throw new ConversationRoomNotFoundException("ID가 " + conversationRoomNo + "인 대화방을 찾을 수 없습니다.");
        }
        entityToDto(room);
    }

    @Override
    public Timestamp readLastConversationTime(long userNo) {
        ConversationRoomEntity room = conversationRoomRepository.findLatestByUserNo(userNo);

        if (room == null){
            return null;
        } else if (room.getConversationRoomEndAt() ==null) {
            return room.getConversationRoomStartAt();
        }
        return room.getConversationRoomEndAt();
    }

    @Override
    public List<ConversationLogResponse> readLatestConversationRoom(long userNo, long conversationRoomNo) {
        ConversationRoomEntity conversationRoom = conversationRoomRepository.findLatestByUserNoExcludingConversationRoomNo(userNo, conversationRoomNo);
        if (conversationRoom != null) {
            List<ConversationLogEntity> conversationLogs = conversationRoom.getConversationLogs();

            // 마지막 5개 가져오기
            int size = conversationLogs.size();
            List<ConversationLogEntity> lastThreeLogs = conversationLogs.subList(Math.max(size - 5, 0), size);

            return lastThreeLogs.stream()
                    .map(conversationLogService::entityToDto)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
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
