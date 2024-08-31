package com.shinhan.knockknock.config;

import com.shinhan.knockknock.domain.entity.NotificationEntity;
import com.shinhan.knockknock.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
    private final NotificationService notificationService;
    /*
        @Scheduled 속성
        fixedRate: 작업 수행시간과 상관없이 일정 주기마다 메소드를 호출하는 것
        fixedDelay는 (작업 수행 시간을 포함하여) 작업을 마친 후부터 주기 타이머가 돌아 메소드를 호출
        initialDelay: 스케줄러에서 메소드가 등록되자마자 수행하는 것이 아닌 초기 지연시간을 설정
        cron: Cron 표현식을 사용하여 작업을 예약
     */
    @Scheduled(fixedDelay = 10000) // 10초
    public void SchedulerTest(){
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .notificationCategory("스케줄러")
                .notificationTitle("스케줄러 테스트")
                .notificationContent("스케줄러 테스트")
                .userNo(1L)
                .build();
        notificationService.notify(notificationEntity);
    }
}
