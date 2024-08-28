package com.shinhan.knockknock.service.conversation;

import com.shinhan.knockknock.domain.dto.conversation.ConversationLogRequest;
import com.shinhan.knockknock.domain.dto.conversation.ConversationLogResponse;
import com.shinhan.knockknock.domain.entity.ConversationLogEntity;
import com.shinhan.knockknock.domain.entity.ConversationRoomEntity;
import com.shinhan.knockknock.repository.ConversationLogRepository;
import com.shinhan.knockknock.repository.ConversationRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConversationLogServiceImpl implements ConversationLogService {

    @Autowired
    ConversationLogRepository conversationLogRepository;

    @Autowired
    ConversationRoomRepository conversationRoomRepository;

    @Override
    public Long createConversationLog(ConversationLogRequest request) {
        Optional<ConversationRoomEntity> conversationRoomOpt = conversationRoomRepository.findById(request.getConversationRoomNo());

        if (conversationRoomOpt.isPresent()) {
            ConversationRoomEntity conversationRoom = conversationRoomOpt.get();
            ConversationLogEntity conversationLog = dtoToEntity(request, conversationRoom);
            return conversationLogRepository.save(conversationLog).getConversationLogNo();
        } else {
            throw new RuntimeException("Conversation Room not found with ID: " + request.getConversationRoomNo());
        }
    }

    @Override
    public List<ConversationLogResponse> readAllConversationLog() {
        return conversationLogRepository.findAll().stream().map(this::entityToDto).toList();
    }

    @Override
    public List<ConversationLogResponse> findLast5ByConversationRoomNo(long conversationRoomNo) {
        return conversationLogRepository.findLast5ByConversationRoomNo(conversationRoomNo).stream().map(this::entityToDto).toList();
    }

    @Override
    public List<ConversationLogResponse> findLastNByConversationRoomNo(int number, long conversationRoomNo) {
        return conversationLogRepository.findLastNByConversationRoomNo(number, conversationRoomNo).stream().map(this::entityToDto).toList();
    }

    @Override
    public void updateConversationLog(long conversationLogNo, ConversationLogRequest request) {
        conversationLogRepository.findById(conversationLogNo)
                .ifPresent(conversationLogEntity -> {
                    conversationLogEntity.setConversationLogInput(request.getConversationLogInput());
                    conversationLogEntity.setConversationLogResponse(request.getConversationLogResponse());
                    conversationLogEntity.setConversationLogToken(request.getConversationLogToken());

                    conversationLogRepository.save(conversationLogEntity);
                });
    }

    @Override
    public void deleteConversationLog(long conversationLogNo) {
        conversationLogRepository.findById(conversationLogNo).ifPresent(conversationLogRepository::delete);
    }

}
