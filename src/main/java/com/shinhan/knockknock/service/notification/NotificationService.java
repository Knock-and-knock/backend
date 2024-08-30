package com.shinhan.knockknock.service.notification;

import com.shinhan.knockknock.domain.dto.notification.ReadNotificationResponse;
import com.shinhan.knockknock.domain.entity.NotificationEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface NotificationService {
    SseEmitter subscribe(Long userNo);
    void notify(NotificationEntity notificationEntity);
    void sendToClient(NotificationEntity notificationEntity);
    SseEmitter createEmitter(Long userNo);
    List<ReadNotificationResponse> readNotifications(Long userNo);
    ReadNotificationResponse readNotification(Long notificationId);
    public Long readNotificationCount(Long userNo);
    public void readAllNotifications(Long userNo);

    // Entity -> DTO
    default ReadNotificationResponse transformEntityToDTO(NotificationEntity notificationEntity) {
        ReadNotificationResponse readNotificationResponse = ReadNotificationResponse
                .builder()
                .notificationNo(notificationEntity.getNotificationNo())
                .notificationCategory(notificationEntity.getNotificationCategory())
                .notificationTitle(notificationEntity.getNotificationTitle())
                .notificationContent(notificationEntity.getNotificationContent())
                .notificationDateTime(String.valueOf(notificationEntity.getNotificationDateTime()))
                .notificationIsCheck(notificationEntity.isNotificationIsCheck())
                .userNo(notificationEntity.getUserNo())
                .build();

        return readNotificationResponse;
    }

}
