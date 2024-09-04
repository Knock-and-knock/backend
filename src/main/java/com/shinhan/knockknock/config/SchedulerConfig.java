package com.shinhan.knockknock.config;

import com.shinhan.knockknock.domain.entity.*;
import com.shinhan.knockknock.repository.*;
import com.shinhan.knockknock.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final MatchRepository matchRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final ConversationRoomRepository conversationRoomRepository;

    /*
        @Scheduled 속성
        fixedRate: 작업 수행시간과 상관없이 일정 주기마다 메소드를 호출하는 것
        fixedDelay는 (작업 수행 시간을 포함하여) 작업을 마친 후부터 주기 타이머가 돌아 메소드를 호출
        initialDelay: 스케줄러에서 메소드가 등록되자마자 수행하는 것이 아닌 초기 지연시간을 설정
        cron: Cron 표현식을 사용하여 작업을 예약 ( 초 분 시 일 월 요일 )
     */

    //@Scheduled(/*cron = "0 0 21 * * ?"*/) 서버와 상관없이 매일 21시에 수행
    @Scheduled(initialDelay = 10000, fixedRate = 24 * 60 * 60 * 1000) // 서버 시작 후 10초 뒤 한번 수행, 이후 24시간 마다 수행
    public void notifyForOldCardHistory() {
        // 조건 2일 (현재 시간 - 2일)
        Timestamp twoDaysAgo = new Timestamp(System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000));

        // 모든 카드 ID 조회
        List<Long> allCardIds = cardRepository.findAllCardIds();

        // 2일 동안 사용 내역이 없는 카드 번호 조회
        List<Long> cardIdsWithoutUse = cardHistoryRepository.findCardIdsWithoutRecentUse(twoDaysAgo);

        // 모든 카드 ID 중 2일 이내에 사용되지 않은 카드 ID만 필터링
        List<Long> cardIdsToNotify = allCardIds.stream()
                .filter(cardIdsWithoutUse::contains)
                .toList();

        // 필터링된 카드들에 대해 매칭된 보호자의 userNo를 찾아 알림 전송
        for (Long cardId : cardIdsToNotify) {
            CardEntity card = cardRepository.findById(cardId).orElse(null);
            if (card != null) {
                Long userNo = card.getUserNo();
                // 가족 카드인 경우 Kname과 phone을 사용해 실제 사용자 식별
                if (card.isCardIsfamily() && card.getCardUserKname() != null && card.getCardUserPhone() != null) {
                    UserEntity actualUser = userRepository.findByUserNameAndUserPhone(
                            card.getCardUserKname(), card.getCardUserPhone()).orElse(null);
                    if (actualUser != null) {
                        userNo = actualUser.getUserNo();
                    }
                }

                // 매칭된 보호자 찾기
                MatchEntity matchEntity = matchRepository.findByUserProtege_UserNo(userNo).orElse(null);
                if (matchEntity != null && matchEntity.getUserProtector() != null) {
                    Long protectorNo = matchEntity.getUserProtector().getUserNo();
                    NotificationEntity protectorNotification = NotificationEntity.builder()
                            .notificationCategory("이상 징후")
                            .notificationTitle("매칭된 사용자 카드 미사용 2일 이상 경과")
                            .notificationContent("매칭된 사용자의 카드가 2일 이상 사용되지 않았습니다. 상황을 확인해 주세요.")
                            .userNo(protectorNo)
                            .build();
                    notificationService.notify(protectorNotification);
                }
            }
        }
    }

    //@Scheduled(/*cron = "0 0 21 * * ?"*/) 서버와 상관없이 매일 21시에 수행
    @Scheduled(initialDelay = 10000, fixedRate = 24 * 60 * 60 * 1000) // 서버 시작 후 10초 뒤 한번 수행, 이후 24시간 마다 수행
    public void notifyForOldConversation(){
        // 조건 3일 (현재 시간 - 3일)
        Timestamp threeDaysAgo = new Timestamp(System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000));

        // 3일 동안 사용 내역이 없는 conversationRoomEntity 조회 (startAt 기준)
        List<ConversationRoomEntity> oldRooms = conversationRoomRepository.findRoomsInactiveSince(threeDaysAgo);

        // 조회된 대화방에 대해 매칭된 보호자의 userNo를 찾아 알림 전송
        for (ConversationRoomEntity conversationRoomEntity : oldRooms){
            Long userNo = conversationRoomEntity.getUser().getUserNo();
            MatchEntity matchEntity = matchRepository.findByUserProtege_UserNo(userNo).orElse(null);
            if (matchEntity != null && matchEntity.getUserProtector() != null) {
                Long protectorNo = matchEntity.getUserProtector().getUserNo();
                NotificationEntity protectorNotification = NotificationEntity.builder()
                        .notificationCategory("이상 징후")
                        .notificationTitle("매칭된 사용자 똑똑이 미사용 3일 이상 경과")
                        .notificationContent("매칭된 사용자가 똑똑이를 3일 이상 사용하지 않았습니다. 상황을 확인해 주세요.")
                        .userNo(protectorNo)
                        .build();
                notificationService.notify(protectorNotification);
            }
        }
    }
}
