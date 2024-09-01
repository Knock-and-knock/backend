package com.shinhan.knockknock.config;

import com.shinhan.knockknock.domain.dto.cardhistory.CreateCardHistoryRequest;
import com.shinhan.knockknock.service.cardhistory.CardHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Random;

@Component
@RequiredArgsConstructor
//@EnableScheduling
public class cardHistorySchedulerConfig {
    private final CardHistoryService cardHistoryService;

    //@Scheduled(fixedRate = 30000) // 30초마다 실행
    public void createCardHistoryAtFixedRate() {
        try {
            // 랜덤 금액 생성 (100,000원부터 3,000,000원까지 50,000원 단위)
            int[] possibleAmounts = new int[59];
            for (int i = 0; i < 59; i++) {
                possibleAmounts[i] = 100000 + (i * 50000);
            }
            int randomAmount = possibleAmounts[new Random().nextInt(possibleAmounts.length)];

            // 예시 데이터 생성
            CreateCardHistoryRequest request = CreateCardHistoryRequest.builder()
                    .cardHistoryAmount(randomAmount)  // 랜덤 금액 설정
                    .cardHistoryShopname("테스트 상점")  // 예시 상점명
                    .cardHistoryApprove(new Timestamp(System.currentTimeMillis()))  // 현재 시간으로 설정
                    .cardCategoryNo(1L)  // 예시 카테고리 번호
                    .cardId(1L)  // 예시 카드 ID
                    .isCardFamily(true)  // 가족 카드 여부
                    .cardHistoryIsCansle(false)  // 취소 여부
                    .build();

            // 서비스 호출을 통한 카드 내역 생성
            cardHistoryService.createCardHistory(request);

            // 로그 기록 (선택 사항)
            System.out.println("카드 내역이 성공적으로 생성되었습니다. 금액: " + randomAmount + "원");
        } catch (Exception e) {
            // 오류 발생 시 로그 기록
            System.err.println("카드 내역 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}

