package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.conversation.ConversationLogRequest;
import com.shinhan.knockknock.domain.dto.conversation.ConversationLogResponse;
import com.shinhan.knockknock.domain.entity.ConversationLogEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.repository.ConversationLogRepository;
import com.shinhan.knockknock.repository.UserRepository;
import com.shinhan.knockknock.service.conversation.ConversationLogService;
import com.shinhan.knockknock.service.conversation.ConversationRoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class ConversationLogTest {

    @Autowired
    ConversationLogService conversationLogService;

    @Autowired
    ConversationLogRepository conversationLogRepository;

    @Autowired
    ConversationRoomService conversationRoomService;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("대화 내역 생성 테스트")
    public void testCreateConversationRoom() {
        // Given
        UserEntity user = userRepository.findAll().get(0);
        long userNo = user.getUserNo();
        Long conversationRoomNo = conversationRoomService.createConversationRoom(userNo);

        ConversationLogRequest request = ConversationLogRequest.builder()
                .conversationLogInput("input")
                .conversationLogResponse("response")
                .conversationLogToken(100)
                .conversationRoomNo(conversationRoomNo)
                .build();

        // When
        Long conversationLogNo = conversationLogService.createConversationLog(request);

        // Then
        ConversationLogEntity savedEntity = conversationLogRepository.findById(conversationLogNo).orElse(null);
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getConversationLogInput()).isEqualTo("input");
        assertThat(savedEntity.getConversationLogResponse()).isEqualTo("response");
        assertThat(savedEntity.getConversationLogToken()).isEqualTo(100);
        assertThat(savedEntity.getConversationRoom().getConversationRoomNo()).isEqualTo(conversationRoomNo);
    }

    @Test
    @DisplayName("대화 내역 전체 조회 테스트")
    public void testReadAllConversationLog() {
        // Given
        UserEntity user = userRepository.findAll().get(0);
        long userNo = user.getUserNo();
        Long conversationRoomNo = conversationRoomService.createConversationRoom(userNo);

        ConversationLogRequest request = ConversationLogRequest.builder()
                .conversationLogInput("input")
                .conversationLogResponse("response")
                .conversationLogToken(100)
                .conversationRoomNo(conversationRoomNo)
                .build();
        conversationLogService.createConversationLog(request);

        // When
        List<ConversationLogResponse> ConversationLogList = conversationLogService.readAllConversationLog();

        // Then
        assertThat(ConversationLogList).hasSize(1);
        ConversationLogResponse ConversationLog = ConversationLogList.get(0);
        assertThat(ConversationLog.getConversationLogInput()).isEqualTo("input");
        assertThat(ConversationLog.getConversationLogResponse()).isEqualTo("response");
        assertThat(ConversationLog.getConversationLogToken()).isEqualTo(100);
        assertThat(ConversationLog.getConversationRoomNo()).isEqualTo(conversationRoomNo);
    }

    @Test
    @DisplayName("대화 내역 최근 5개 조회 테스트")
    public void testFindLast5ByConversationRoomNo() {
        // Given
        UserEntity user = userRepository.findAll().get(0);
        long userNo = user.getUserNo();
        Long conversationRoomNo = conversationRoomService.createConversationRoom(userNo);

        for (int i = 0; i < 6; i++) {
            ConversationLogRequest request = ConversationLogRequest.builder()
                    .conversationLogInput("input" + i)
                    .conversationLogResponse("response" + i)
                    .conversationLogToken(100 + i)
                    .conversationRoomNo(conversationRoomNo)
                    .build();
            conversationLogService.createConversationLog(request);
        }

        // When
        List<ConversationLogResponse> ConversationLogList = conversationLogService.findLast5ByConversationRoomNo(conversationRoomNo);

        // Then
        assertThat(ConversationLogList).hasSize(5);
        ConversationLogResponse ConversationLog = ConversationLogList.get(ConversationLogList.size() - 1);
        assertThat(ConversationLog.getConversationLogInput()).isEqualTo("input5");
        assertThat(ConversationLog.getConversationLogResponse()).isEqualTo("response5");
        assertThat(ConversationLog.getConversationLogToken()).isEqualTo(105);
        assertThat(ConversationLog.getConversationRoomNo()).isEqualTo(conversationRoomNo);
    }

    @Test
    @DisplayName("대화 내역 수정 테스트")
    public void testUpdateConversationLog() {
        // Given
        UserEntity user = userRepository.findAll().get(0);
        long userNo = user.getUserNo();
        Long conversationRoomNo = conversationRoomService.createConversationRoom(userNo);

        ConversationLogRequest request = ConversationLogRequest.builder()
                .conversationLogInput("input")
                .conversationLogResponse("response")
                .conversationLogToken(100)
                .conversationRoomNo(conversationRoomNo)
                .build();
        Long conversationLogNo = conversationLogService.createConversationLog(request);

        // When
        ConversationLogRequest updateRequest = ConversationLogRequest.builder()
                .conversationLogInput("update input")
                .conversationLogResponse("update response")
                .conversationLogToken(1000)
                .build();
        conversationLogService.updateConversationLog(conversationLogNo, updateRequest);

        // Then
        ConversationLogEntity savedEntity = conversationLogRepository.findById(conversationLogNo).orElse(null);
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getConversationLogInput()).isEqualTo("update input");
        assertThat(savedEntity.getConversationLogResponse()).isEqualTo("update response");
        assertThat(savedEntity.getConversationLogToken()).isEqualTo(1000);
        assertThat(savedEntity.getConversationRoom().getConversationRoomNo()).isEqualTo(conversationRoomNo);
    }

    @Test
    @DisplayName("대화 내역 삭제 테스트")
    public void testDeleteConversationLog() {
        // Given
        UserEntity user = userRepository.findAll().get(0);
        long userNo = user.getUserNo();
        Long conversationRoomNo = conversationRoomService.createConversationRoom(userNo);

        ConversationLogRequest request = ConversationLogRequest.builder()
                .conversationLogInput("input")
                .conversationLogResponse("response")
                .conversationLogToken(100)
                .conversationRoomNo(conversationRoomNo)
                .build();
        Long conversationLogNo = conversationLogService.createConversationLog(request);

        // When
        conversationLogService.deleteConversationLog(conversationLogNo);

        // Then
        ConversationLogEntity savedEntity = conversationLogRepository.findById(conversationLogNo).orElse(null);
        assertThat(savedEntity).isNull();
    }
}
