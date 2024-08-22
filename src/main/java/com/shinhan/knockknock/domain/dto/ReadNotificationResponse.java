package com.shinhan.knockknock.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

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
