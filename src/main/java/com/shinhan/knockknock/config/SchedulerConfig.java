package com.shinhan.knockknock.config;

import com.shinhan.knockknock.domain.entity.CardHistoryEntity;
import com.shinhan.knockknock.domain.entity.MatchEntity;
import com.shinhan.knockknock.domain.entity.NotificationEntity;
import com.shinhan.knockknock.repository.CardHistoryRepository;
import com.shinhan.knockknock.repository.MatchRepository;
import com.shinhan.knockknock.repository.UserRepository;
import com.shinhan.knockknock.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
    private final NotificationService notificationService;
    private final CardHistoryRepository cardHistoryRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    //@Scheduled(fixedDelay = 60000)
    public void notifyForOldCardHistory() {
        // 현재 날짜에서 2일 전 날짜 계산
        Timestamp twoDaysAgo = new Timestamp(System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000));

        // 조건에 맞는 cardHistory 조회
        List<CardHistoryEntity> cardHistories = cardHistoryRepository.findByCardHistoryApproveBefore(twoDaysAgo);


        for (CardHistoryEntity cardHistory : cardHistories) {
            Long cardId = cardHistory.getCardId();
            Long userNo = cardHistory.getCard().getUserNo();

            // 해당 userNo에게 알림을 보냅니다.
            NotificationEntity notificationEntity = NotificationEntity.builder()
                    .notificationCategory("카드 내역 알림")
                    .notificationTitle("마지막 카드 사용 내역")
                    .notificationContent("마지막 카드 사용 내역이 2일 이상 지난 항목이 있습니다.")
                    .userNo(userNo)
                    .build();
            notificationService.notify(notificationEntity);

            // 보호자가 있다면 보호자에게도 알림을 보냅니다.
            MatchEntity matchEntity = matchRepository.findByUserProtege_UserNo(userNo).orElse(null);
            if (matchEntity != null && matchEntity.getUserProtector() != null) {
                Long protectorNo = matchEntity.getUserProtector().getUserNo();
                NotificationEntity protectorNotification = NotificationEntity.builder()
                        .notificationCategory("마지막 카드 내역 알림")
                        .notificationTitle("보호자 알림")
                        .notificationContent("보호자에게 연결된 사용자의 카드 사용 내역이 2일 이상 지난 항목이 있습니다.")
                        .userNo(protectorNo)
                        .build();
                notificationService.notify(protectorNotification);
            }
        }
    }
    /*
        @Scheduled 속성
        fixedRate: 작업 수행시간과 상관없이 일정 주기마다 메소드를 호출하는 것
        fixedDelay는 (작업 수행 시간을 포함하여) 작업을 마친 후부터 주기 타이머가 돌아 메소드를 호출
        initialDelay: 스케줄러에서 메소드가 등록되자마자 수행하는 것이 아닌 초기 지연시간을 설정
        cron: Cron 표현식을 사용하여 작업을 예약
     */
    //@Scheduled(fixedDelay = 10000 * 6 * 60)
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
