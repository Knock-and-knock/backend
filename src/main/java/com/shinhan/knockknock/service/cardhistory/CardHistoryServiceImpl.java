package com.shinhan.knockknock.service.cardhistory;

import com.shinhan.knockknock.domain.dto.cardhistory.CreateCardHistoryRequest;
import com.shinhan.knockknock.domain.dto.cardhistory.DetailCardHistoryResponse;
import com.shinhan.knockknock.domain.dto.cardhistory.ReadCardHistoryResponse;
import com.shinhan.knockknock.domain.entity.*;
import com.shinhan.knockknock.repository.CardCategoryRepository;
import com.shinhan.knockknock.repository.CardHistoryRepository;
import com.shinhan.knockknock.repository.CardRepository;
import com.shinhan.knockknock.repository.UserRepository;
import com.shinhan.knockknock.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@Service
@EnableAsync  // 비동기 처리를 활성화
@Slf4j
public class CardHistoryServiceImpl implements CardHistoryService {

    @Autowired
    private CardHistoryRepository cardHistoryRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CardCategoryRepository cardCategoryRepository;

    @Override
    public Long createCardHistory(CreateCardHistoryRequest request) {
        try {
            // 카드 엔티티가 존재하는지 확인
            CardEntity card = cardRepo.findById(request.getCardId())
                    .orElseThrow(() -> new RuntimeException("해당 카드가 존재하지 않습니다."));

            // CardHistoryEntity 생성 및 저장
            CardHistoryEntity newCardHistory = cardHistoryRepo.save(dtoToEntity(request));
            Long cardHistoryNo = newCardHistory.getCardHistoryNo();

            Long userNo = card.getUserNo();

            // 비동기로 탐지 기능 실행
            detectFraudulentTransaction(newCardHistory.getCard().getCardId(), newCardHistory.getCardHistoryAmount(), userNo);

            return cardHistoryNo;
        } catch (DataAccessException e) {
            throw new RuntimeException("카드 내역 생성 중 데이터베이스 오류가 발생했습니다.", e);
        } catch (Exception e) {
            throw new RuntimeException("카드 내역 생성에 실패했습니다.", e);
        }
    }


    @Async  // 비동기 처리 메서드
    public void detectFraudulentTransaction(Long cardId, int newTransactionAmount, Long userNo) {
        System.out.println(userNo);
        try {
            // 최근 거래내역 30개를 가져옴
            List<CardHistoryEntity> cardHistoryList = cardHistoryRepo.findByCard_CardId(cardId, PageRequest.of(0, 30, Sort.by(Sort.Direction.DESC, "cardHistoryApprove")));

            // 거래 내역이 있을 경우 평균 계산
            double threshold;
            if (!cardHistoryList.isEmpty()) {
                // 결제 금액 리스트 생성
                List<Integer> transactionAmounts = cardHistoryList.stream()
                        .map(CardHistoryEntity::getCardHistoryAmount)
                        .collect(Collectors.toList());

                // 평균값 계산
                double average = calculateTransactionAverage(transactionAmounts);

                // 임계값 설정 (평균값의 3배와 600000 중 더 큰 값)
                threshold = Math.max(average * 3, 600000);
            } else {
                // 거래 내역이 없을 때 임계값 설정
                threshold = 600000;
            }

            // 탐지 결과 확인 및 알림 발송
            // newTransactionAmount가 String 타입이라면 숫자형으로 변환
            int amount = Integer.parseInt(String.valueOf(newTransactionAmount));

            if (amount > threshold) {
                // 천 단위로 쉼표 추가
                String formattedAmount = String.format("%,d", amount);
                String notificationContent = formattedAmount + "원 결제되어 이상결제가 탐지 되었습니다.";

                NotificationEntity notificationEntity = NotificationEntity.builder()
                        .notificationCategory("이상 거래")
                        .notificationTitle("이상거래 알림")
                        .notificationContent(notificationContent)
                        .userNo(userNo)
                        .build();

                notificationService.notify(notificationEntity);
            }
        } catch (Exception e) {
            throw new RuntimeException("이상 거래 탐지 중 오류가 발생했습니다.", e);
        }
    }

    public double calculateTransactionAverage(List<Integer> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return 0;  // 거래 내역이 없을 경우 0 반환
        }

        double sum = 0;
        for (double transaction : transactions) {
            sum += transaction;
        }
        return sum / transactions.size();
    }


    @Override
    public List<ReadCardHistoryResponse> readAll(Long cardId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "cardHistoryApprove");

        try {
            List<CardHistoryEntity> entityList = cardHistoryRepo.findByCard_CardId(cardId, sort);
            return entityList.stream().map(this::entityToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("카드 내역 조회에 실패했습니다.", e);
        }
    }

    @Override
    public List<ReadCardHistoryResponse> readAllWithinDateRange(Long cardId, Timestamp startDate, Timestamp endDate) {
        Sort sort = Sort.by(Sort.Direction.DESC, "cardHistoryApprove");

        try {
            List<CardHistoryEntity> entityList = cardHistoryRepo.findByCard_CardIdAndCardHistoryApproveBetween(cardId, startDate, endDate, sort);
            return entityList.stream().map(this::entityToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("카드 내역 조회에 실패했습니다.", e);
        }
    }

    @Override
    public String readAllWithinDateRangeForConversation(Long cardId, Timestamp startDate, Timestamp endDate) {
        List<ReadCardHistoryResponse> cardHistoryList = readAllWithinDateRange(cardId, startDate, endDate);

        // 총 사용 내역 개수 계산
        int totalTransactions = cardHistoryList.size();

        // 조회 기간을 문자열로 변환
        String dateRange = "Period: " + startDate.toString() + " to " + endDate.toString() + "\n";

        // StringBuilder를 사용하여 문자열을 생성
        StringBuilder result = new StringBuilder();
        result.append(dateRange);
        result.append("Total Transactions: ").append(totalTransactions).append("\n");
        result.append("Showing the first 10 transactions:\n");
        result.append("--------------------------------\n");

        // 최대 10개의 사용 내역만 문자열로 변환
        for (int i = 0; i < Math.min(10, totalTransactions); i++) {
            ReadCardHistoryResponse history = cardHistoryList.get(i);
            result.append("Amount: ").append(history.getCardHistoryAmount()).append(" KRW").append("\n")
                    .append("Shop: ").append(history.getCardHistoryShopname()).append("\n")
                    .append("Approval Date: ").append(history.getCardHistoryApprove()).append("\n")
                    .append("Is Cancelled: ").append(history.isCardHistoryIsCansle() ? "Yes" : "No").append("\n")
                    .append("--------------------------------\n");
        }

        return result.toString();
    }

    public DetailCardHistoryResponse readDetail(Long cardHistoryNo) {
        Optional<CardHistoryEntity> entity = cardHistoryRepo.findById(cardHistoryNo);

        // Optional에서 entityToDtoDetail로 변환 후 반환
        return entity.map(this::entityToDtoDetail)
                .orElse(null); // 또는 적절한 기본값이나 예외 처리
    }

    @Override
    public String findUserNameForFamilyCard(CardEntity card) {
        UserEntity relatedUser = userRepository.findByCards_CardBankAndCards_CardAccountAndCards_CardIsfamilyFalse(
                        card.getCardBank(), card.getCardAccount())
                .orElseThrow(() -> new NoSuchElementException("관련 사용자를 찾을 수 없습니다."));
        return relatedUser.getUserName();
    }

    @Override
    public CardEntity readTopUsedCardLastMonth(Long userNo) {
        long cardNo = cardHistoryRepo.findTopUsedCardNoLastMonthByUser(userNo).orElse(0L);
        return cardRepo.findById(cardNo).orElse(null);
    }

    @Transactional
    public void cancelCardHistory(Long cardHistoryNo) {
        CardHistoryEntity cardHistory = cardHistoryRepo.findById(cardHistoryNo)
                .orElseThrow(() -> new NoSuchElementException("해당 카드 내역이 존재하지 않습니다."));

        cardHistory.setCardHistoryIsCansle(true);
        cardHistoryRepo.save(cardHistory);
    }

    @Override
    public CardHistoryEntity dtoToEntity(CreateCardHistoryRequest request) {
        return CardHistoryEntity.builder()
                .cardHistoryAmount(request.getCardHistoryAmount())
                .cardHistoryShopname(request.getCardHistoryShopname())
                .cardHistoryApprove(new Timestamp(System.currentTimeMillis())) // 예시로 현재 시간 사용
                .cardCategory(CardCategoryEntity.builder().cardCategoryNo(request.getCardCategoryNo()).build())
                .card(CardEntity.builder().cardId(request.getCardId()).build())
                .build();
    }

    @Override
    public DetailCardHistoryResponse entityToDtoDetail(CardHistoryEntity entity) {
        String cardCategoryName = String.valueOf(cardCategoryRepository.findCardCategoryNameByCardCategoryNo(entity.getCardCategoryNo()));
        return DetailCardHistoryResponse.builder()
                .cardHistoryAmount(entity.getCardHistoryAmount())
                .cardHistoryShopname(entity.getCardHistoryShopname())
                .cardHistoryApprove(entity.getCardHistoryApprove())
                .cardCategoryName(cardCategoryName)
                .isCardFamily(entity.isCardFamily())
                .cardHistoryIsCansle(entity.getCardHistoryIsCansle() != null && entity.getCardHistoryIsCansle())
                .build();
    }
    @Override
    public ReadCardHistoryResponse entityToDto(CardHistoryEntity entity) {
        return ReadCardHistoryResponse.builder()
                .cardHistoryNo(entity.getCardHistoryNo())
                .cardHistoryAmount(entity.getCardHistoryAmount())
                .cardHistoryShopname(entity.getCardHistoryShopname())
                .cardHistoryApprove(entity.getCardHistoryApprove())
                .isCardFamily(entity.isCardFamily())
                .cardHistoryIsCansle(entity.getCardHistoryIsCansle() != null && entity.getCardHistoryIsCansle())
                .build();
    }
}
