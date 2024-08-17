package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.conversationroom.ConversationLogRequest;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationLogResponse;
import com.shinhan.knockknock.domain.entity.ConversationLogEntity;
import com.shinhan.knockknock.repository.ConversationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationLogServiceImpl implements ConversationLogService {

    @Autowired
    ConversationLogRepository conversationLogRepository;

    @Override
    public Long createConversationLog(ConversationLogRequest request) {
        ConversationLogEntity entity = conversationLogRepository.save(dtoToEntity(request));
        return entity.getConversationLogNo();
    }

    @Override
    public List<ConversationLogResponse> readAllConversationLog() {
        return conversationLogRepository.findAll().stream().map(this::entityToDto).toList();
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
