package com.shinhan.knockknock.service.notification;

import com.shinhan.knockknock.domain.dto.notification.ReadNotificationResponse;
import com.shinhan.knockknock.domain.entity.NotificationEntity;
import com.shinhan.knockknock.repository.EmitterRepository;
import com.shinhan.knockknock.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    public static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    /**
     * 클라이언트가 구독을 위해 호출하는 메서드.
     *
     * @param userNo - 구독하는 클라이언트의 사용자 아이디.
     * @return SseEmitter - 서버에서 보낸 이벤트 Emitter
     */
    public SseEmitter subscribe(Long userNo) {
        SseEmitter emitter = createEmitter(userNo);

        NotificationEntity notificationEntity = NotificationEntity
                .builder()
                .notificationCategory("connection category")
                .notificationTitle("connection title")
                .notificationContent("connection content")
                .userNo(userNo)
                .notificationIsCheck(true)
                .build();

        sendToClient(notificationEntity);
        return emitter;
    }

    /**
     * 서버의 이벤트를 클라이언트에게 보내는 메서드
     *
     * @param notificationEntity  - 전송 정보 객체.
     */
    public void notify(NotificationEntity notificationEntity) {
        sendToClient(notificationEntity);
    }

    /**
     * 클라이언트에게 데이터를 전송
     *
     * @param notificationEntity - 전송 정보 객체.
     */
    public void sendToClient(NotificationEntity notificationEntity) {
        Long userNo = notificationEntity.getUserNo();
        SseEmitter emitter = emitterRepository.get(userNo);

        // 현재 시간을 java에서 찍어서 DB 저장과 실시간 알림에 모두 사용
        // 이후에 알림 조회할 때 데이터 차이가 없게
        notificationEntity.setNotificationDateTime(new Timestamp(System.currentTimeMillis()));  // 변경된 부분
        notificationRepository.save(notificationEntity);

        ReadNotificationResponse notificationResponse = transformEntityToDTO(notificationEntity);
        if (emitter != null) {
            try {
                String eventName = "userNotification_" + userNo;
                emitter.send(SseEmitter.event().id(String.valueOf(userNo)).name(eventName).data(notificationResponse));
            } catch (IOException exception) {
                emitterRepository.deleteByUserNo(userNo);
                throw new RuntimeException("연결 오류!");
            }
        }
    }


    /**
     * 사용자 아이디를 기반으로 이벤트 Emitter를 생성
     *
     * @param userNo - 사용자 아이디.
     * @return SseEmitter - 생성된 이벤트 Emitter.
     */
    public SseEmitter createEmitter(Long userNo) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userNo, emitter);

        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        emitter.onCompletion(() -> emitterRepository.deleteByUserNo(userNo));
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> emitterRepository.deleteByUserNo(userNo));

        return emitter;
    }

    /**
     * 사용자 아이디를 기반으로 알림 전체 조회
     *
     * @param userNo - 사용자 아이디.
     * @return List<ReadNotificationResponse> - 해당 사용자 모든 알림 조회
     */
    public List<ReadNotificationResponse> readNotifications(Long userNo){
        return notificationRepository.findByUserNoOrderByNotificationDateTimeDesc(userNo)
                .stream()
                .map(this::transformEntityToDTO)
                .toList();
    }

    /**
     * 알림 아이디를 기반으로 알림 상세 조회
     *
     * @param notificationNo - 알림 아이디
     * @return ReadNotificationResponse - 알림 내용 전송
     */
    public ReadNotificationResponse readNotification(Long notificationNo){
        notificationRepository.readCheckedByNotificationNo(notificationNo);
        return notificationRepository.findById(notificationNo)
                .map(this::transformEntityToDTO) // Optional이 비어 있지 않다면 transformEntityToDTO 호출
                .orElseThrow(() -> new NoSuchElementException("not found for id: " + notificationNo)); // Optional이 비어있을 때 예외 처리
    }

    /**
     * 사용자가 읽지 않은 알림 개수 조회
     *
     * @param userNo - 사용자 아이디.
     * @return ReadNotificationResponse - 알림 내용 전송
     */
    public Long readNotificationCount(Long userNo){
        return notificationRepository.countByUserNoAndNotificationIsCheck(userNo, false);
    };

    public void readAllNotifications(Long userNo){
        notificationRepository.readAllCheckedByUserNo(userNo);
    };

    /**
     * 소비 내역 생성이 없는 사용자에게 알림
     */
}