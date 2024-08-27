package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.entity.NotificationEntity;
import com.shinhan.knockknock.repository.EmitterRepository;
import com.shinhan.knockknock.repository.NotificationRepository;
import com.shinhan.knockknock.service.notification.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @Mock
    private EmitterRepository emitterRepository; // EmitterRepository를 모킹하여 사용

    @Mock
    private NotificationRepository notificationRepository; // NotificationRepository를 모킹하여 사용

    @InjectMocks
    private NotificationServiceImpl notificationService; // 모킹된 의존성을 주입받은 NotificationServiceImpl 인스턴스

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // 필요한 mock 객체들을 초기화
    }

    @DisplayName("클라이언트에게 알림이 정상적으로 전송되는지 테스트: notify 메서드")
    @Test
    void notifySendsNotification() throws IOException {
        // Given
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .notificationCategory("Test Category")
                .notificationTitle("Test Title")
                .notificationContent("Test Content")
                .userNo(1L)
                .build();

        SseEmitter emitter = new SseEmitter(); // 새로운 SseEmitter 인스턴스 생성
        when(emitterRepository.get(1L)).thenReturn(emitter); // EmitterRepository에서 SseEmitter를 반환하도록 설정

        // When
        notificationService.notify(notificationEntity); // notify 메서드 호출

        // Then
        verify(notificationRepository).save(notificationEntity);
        verify(emitterRepository).get(1L); // EmitterRepository에서 해당 사용자 번호로 SseEmitter를 가져왔는지 확인
    }

    @DisplayName("클라이언트에게 알림이 정상적으로 전송되는지 테스트: sendToClient 메서드")
    @Test
    void sendToClientSendsEvent() throws IOException {
        // Given
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .notificationCategory("Test Category")
                .notificationTitle("Test Title")
                .notificationContent("Test Content")
                .userNo(1L)
                .build();

        SseEmitter emitter = mock(SseEmitter.class); // SseEmitter를 모킹하여 사용
        when(emitterRepository.get(1L)).thenReturn(emitter); // EmitterRepository에서 모킹된 SseEmitter를 반환하도록 설정

        // When
        notificationService.sendToClient(notificationEntity); // sendToClient 메서드 호출

        // Then
        verify(notificationRepository).save(notificationEntity);
        verify(emitter).send(any(SseEmitter.SseEventBuilder.class)); // SseEmitter를 통해 이벤트가 전송되었는지 확인
    }

    // Emitter 전송이 실패할 경우, Emitter가 올바르게 삭제되는지 테스트
    @Test
    void sendToClientRemovesEmitte() throws IOException {
        // Given
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .notificationCategory("Test Category")
                .notificationTitle("Test Title")
                .notificationContent("Test Content")
                .userNo(1L)
                .build();

        SseEmitter emitter = mock(SseEmitter.class); // SseEmitter를 모킹하여 사용
        when(emitterRepository.get(1L)).thenReturn(emitter); // EmitterRepository에서 모킹된 SseEmitter를 반환하도록 설정
        doThrow(new IOException()).when(emitter).send(any(SseEmitter.SseEventBuilder.class)); // SseEmitter가 예외를 발생시키도록 설정

        // When & Then
        assertThrows(RuntimeException.class, () -> notificationService.sendToClient(notificationEntity)); // 예외가 발생하는지 확인
        verify(emitterRepository).deleteByUserNo(1L); // 예외 발생 시 Emitter가 삭제되었는지 확인
    }
}
