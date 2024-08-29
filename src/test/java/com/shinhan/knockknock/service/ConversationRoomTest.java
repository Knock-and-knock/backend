package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.conversation.ConversationRoomResponse;
import com.shinhan.knockknock.domain.dto.conversation.ConversationRoomUpdateRequest;
import com.shinhan.knockknock.domain.entity.ConversationRoomEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.repository.ConversationRoomRepository;
import com.shinhan.knockknock.repository.UserRepository;
import com.shinhan.knockknock.service.conversation.ConversationRoomService;
import com.shinhan.knockknock.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class ConversationRoomTest {

    @Autowired
    ConversationRoomService conversationRoomService;

    @Autowired
    ConversationRoomRepository conversationRoomRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("대화방 생성 테스트")
    public void testCreateConversationRoom() {
        // Given
        UserEntity user = userRepository.findAll().get(0);
        long userNo = user.getUserNo();

        // When
        Long conversationRoomNo = conversationRoomService.createConversationRoom(userNo);

        // Then
        ConversationRoomEntity savedEntity = conversationRoomRepository.findById(conversationRoomNo).orElse(null);

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getUser().getUserNo()).isEqualTo(userNo);
    }

    @Test
    @DisplayName("대화방 전체 조회 테스트")
    public void testReadAllConversationRoom() {
        // Given
        UserEntity user = userRepository.findAll().get(0);
        long userNo = user.getUserNo();
        conversationRoomService.createConversationRoom(userNo);

        // When
        List<ConversationRoomResponse> conversationRoomList = conversationRoomService.readAllConversationRoom();

        // Then
        assertThat(conversationRoomList).hasSize(1);
        ConversationRoomResponse conversationRoom = conversationRoomList.get(0);
        assertThat(conversationRoom.getUserNo()).isEqualTo(userNo);
    }

    @Test
    @DisplayName("대화방 수정 테스트")
    public void testUpdateConversationRoom() {
        // Given
        UserEntity user = userRepository.findAll().get(0);
        long userNo = user.getUserNo();
        Long conversationRoomNo = conversationRoomService.createConversationRoom(userNo);

        // When
        LocalDateTime updatedTime = LocalDateTime.of(2024, 8, 19, 12, 0, 0);
        ConversationRoomUpdateRequest request = ConversationRoomUpdateRequest.builder()
                .conversationEndAt(updatedTime)
                .build();
        conversationRoomService.updateConversationRoom(conversationRoomNo, request);

        // Then
        ConversationRoomEntity updatedEntity = conversationRoomRepository.findById(conversationRoomNo).orElse(null);
        assertThat(updatedEntity).isNotNull();
        assertThat(updatedEntity.getConversationRoomEndAt()).isEqualTo(Timestamp.valueOf(updatedTime));
    }

    @Test
    @DisplayName("대화방 종료 시간 수정 테스트")
    public void testUpdateConversationRoomEndAt() {
        // Given
        UserEntity user = userRepository.findAll().get(0);
        long userNo = user.getUserNo();
        Long conversationRoomNo = conversationRoomService.createConversationRoom(userNo);

        // When
        conversationRoomService.updateConversationRoomEndAt(conversationRoomNo);

        // Then
        ConversationRoomEntity updatedEntity = conversationRoomRepository.findById(conversationRoomNo).orElse(null);
        assertThat(updatedEntity).isNotNull();
        assertThat(updatedEntity.getConversationRoomEndAt()).isNotNull();
    }

    @Test
    @DisplayName("대화방 삭제 테스트")
    public void testDeleteConversation() {
        // Given
        UserEntity user = userRepository.findAll().get(0);
        long userNo = user.getUserNo();
        Long conversationRoomNo = conversationRoomService.createConversationRoom(userNo);

        // When
        conversationRoomService.deleteConversation(conversationRoomNo);

        // Then
        ConversationRoomEntity deletedEntity = conversationRoomRepository.findById(conversationRoomNo).orElse(null);
        assertThat(deletedEntity).isNull();
    }

}
