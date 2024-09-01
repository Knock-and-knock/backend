package com.shinhan.knockknock.service.cardhistory;

import com.shinhan.knockknock.domain.dto.cardhistory.CreateCardHistoryRequest;
import com.shinhan.knockknock.domain.dto.cardhistory.DetailCardHistoryResponse;
import com.shinhan.knockknock.domain.dto.cardhistory.ReadCardHistoryResponse;
import com.shinhan.knockknock.domain.entity.CardCategoryEntity;
import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.domain.entity.CardHistoryEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.repository.CardHistoryRepository;
import com.shinhan.knockknock.repository.CardRepository;
import com.shinhan.knockknock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
public class CardHistoryServiceImpl implements CardHistoryService {

    @Autowired
    private CardHistoryRepository cardHistoryRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepo;

    @Override
    public Long createCardHistory(CreateCardHistoryRequest request) {
        try {
            CardHistoryEntity newCardHistory = cardHistoryRepo.save(dtoToEntity(request));
            Long cardHistoryNo = newCardHistory.getCardHistoryNo();

            // 비동기로 탐지 기능 실행
            detectFraudulentTransaction(newCardHistory.getCard().getCardId());

            return cardHistoryNo;
        } catch (DataAccessException e) {
            throw new RuntimeException("카드 내역 생성 중 데이터베이스 오류가 발생했습니다.", e);
        } catch (Exception e) {
            throw new RuntimeException("카드 내역 생성에 실패했습니다.", e);
        }
    }

    @Async  // 비동기 처리 메서드
    public void detectFraudulentTransaction(Long cardId) {
        try {
            // 카드 내역 조회
            List<CardHistoryEntity> cardHistoryList = cardHistoryRepo.findByCard_CardId(cardId, Sort.by(Sort.Direction.DESC, "cardHistoryApprove"));

            // 결제 금액 리스트 생성
            List<Integer> transactionAmounts = cardHistoryList.stream()
                    .map(CardHistoryEntity::getCardHistoryAmount)
                    .collect(Collectors.toList());

            // 탐지 기능 수행
            double threshold = calculateTransactionThreshold(transactionAmounts);

            // 탐지 결과 확인 및 출력
            if (transactionAmounts.stream().anyMatch(amount -> amount > threshold)) {
                System.out.println("이상금융거래가 탐지 되었습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("이상 거래 탐지 중 오류가 발생했습니다.", e);
        }
    }

    public double calculateTransactionThreshold(List<Integer> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return 600000;  // 카드 내역이 없을 때 600000 반환
        }

        double sum = 0;
        for (double transaction : transactions) {
            sum += transaction;
        }
        double average = sum / transactions.size();

        double threshold = average * 3;
        return threshold < 600000 ? 600000 : threshold;
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
        try {
            List<CardHistoryEntity> entityList = cardHistoryRepo.findByCard_CardIdAndCardHistoryApproveBetween(cardId, startDate, endDate);
            return entityList.stream().map(this::entityToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("카드 내역 조회에 실패했습니다.", e);
        }
    }

    public DetailCardHistoryResponse readDetail(Long cardHistoryNo){
        Optional<CardHistoryEntity> entity = cardHistoryRepo.findById(cardHistoryNo);
        return (DetailCardHistoryResponse) entity.stream().map(this::entityToDtoDetail);
    }

//    @Override
//    public String findUserNameForFamilyCard(CardEntity card) {
//        UserEntity relatedUser = userRepository.findByCards_CardBankAndCards_CardAccountAndCards_CardIsfamilyFalse(
//                        card.getCardBank(), card.getCardAccount())
//                .orElseThrow(() -> new NoSuchElementException("관련 사용자를 찾을 수 없습니다."));
//        return relatedUser.getUserName();
//    }

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
        return DetailCardHistoryResponse.builder()
                .cardHistoryAmount(entity.getCardHistoryAmount())
                .cardHistoryShopname(entity.getCardHistoryShopname())
                .cardHistoryApprove(entity.getCardHistoryApprove())
                .cardCategoryNo(entity.getCardCategoryNo())
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
