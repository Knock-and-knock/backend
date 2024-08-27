package com.shinhan.knockknock.domain.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReadNotificationResponse {
    private Long notificationNo;
    private String notificationCategory;
    private String notificationTitle;
    private String notificationContent;
    private String notificationDateTime;
    private boolean notificationIsCheck;
    private Long userNo;
}
