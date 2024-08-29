package com.shinhan.knockknock.service.consumption;

import com.shinhan.knockknock.domain.dto.consumption.ReadConsumptionResponse;
import com.shinhan.knockknock.domain.entity.CardCategoryEntity;
import com.shinhan.knockknock.domain.entity.CardHistoryEntity;
import com.shinhan.knockknock.repository.CardCategoryRepository;
import com.shinhan.knockknock.repository.CardRepository;
import com.shinhan.knockknock.repository.ConsumptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsumptionServiceImpl implements ConsumptionService {
    private final ConsumptionRepository consumptionRepository;
    private final CardRepository cardRepository;
    private final CardCategoryRepository cardCategoryRepository;

    // userNo로 cardNo 조회
    public List<String> readCardNoByUserNo(Long userNo) {
        return cardRepository.findCardNoByUserNo(userNo);
    }

    /**
     * 주어진 사용자 번호와 날짜 범위를 기반으로 월별 소비 리포트를 생성
     *
     * @param userNo - 사용자 번호
     * @param startDate - 검색 기준 시작일
     * @param endDate - 검색 기준 마감일
     * @return {
     *     "cardId": 1,
     *     "categoryName": "교통",
     *     "totalAmount": 218000,
     *     "amount": 25000,
     *     "family": false
     *   },
     */
    public List<ReadConsumptionResponse> readConsumptionReportList(Long userNo, Date startDate, Date endDate) {
        List<String> cardNos = readCardNoByUserNo(userNo);

        List<CardHistoryEntity> cardHistories = consumptionRepository
                .findCardHistoriesByCard_CardNoInAndCardHistoryApproveBetween(cardNos, startDate, endDate);

        // 카테고리별, 카드 번호별로 금액을 합산하여 Map으로 저장
        Map<String, Map<String, Integer>> categorySumMap = cardHistories.stream()
                .collect(Collectors.groupingBy(
                        // 카드 이력의 카테고리 이름을 기준으로 그룹화
                        cardHistory -> cardHistory.getCardCategory().getCardCategoryName(),
                        // 각 카테고리 내에서 카드 번호로 다시 그룹화하고, 금액을 합산
                        Collectors.groupingBy(
                                cardHistory -> cardHistory.getCard().getCardNo(),
                                Collectors.summingInt(CardHistoryEntity::getCardHistoryAmount)
                        )
                ));

        // 모든 카테고리와 카드에서의 총 소비 금액을 계산
        int totalAmount = categorySumMap.values().stream()
                .flatMap(map -> map.values().stream())
                .mapToInt(Integer::intValue)
                .sum();

        // 각 카테고리와 카드별로 리포트 생성하고 리스트로 반환
        return categorySumMap.entrySet().stream()
                .flatMap(categoryEntry -> categoryEntry.getValue().entrySet().stream()
                        .map(cardEntry -> ReadConsumptionResponse.builder()
                                .categoryName(categoryEntry.getKey())
                                .amount(cardEntry.getValue())
                                .totalAmount(totalAmount)
                                .cardId(cardHistories.stream()
                                        .filter(ch -> ch.getCard().getCardNo().equals(cardEntry.getKey()))
                                        .findFirst()
                                        .map(ch -> ch.getCard().getCardId())
                                        .orElse(null))
                                .build()
                        )
                ).collect(Collectors.toList());
    }

    /**
     * 주어진 cardId와 startDate, endDate로 소비 리포트를 생성
     *
     * @param cardId - 카드 식별번호
     * @param startDate - 검색 기준 시작일
     * @param endDate - 검색 기준 마감일
     * @return List<ReadConsumptionResponse>
     */
    public List<ReadConsumptionResponse> readConsumptionReport(Long cardId, java.sql.Date startDate, java.sql.Date endDate) {
        // 모든 카테고리를 조회
        List<CardCategoryEntity> categories = cardCategoryRepository.findAll();

        // 카드 ID와 날짜 범위로 소비 내역 조회
        List<CardHistoryEntity> cardHistories = consumptionRepository.findCardHistoriesByCardIdAndDateRange(cardId, startDate, endDate);

        // 카테고리별 금액 합산
        Map<String, Integer> categoryAmountMap = cardHistories.stream()
                .collect(Collectors.groupingBy(
                        cardHistory -> cardHistory.getCardCategory().getCardCategoryName(),
                        Collectors.summingInt(CardHistoryEntity::getCardHistoryAmount)
                ));

        // 총 금액 계산
        int totalAmount = categoryAmountMap.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        // 리포트 생성
        return categories.stream()
                .map(category -> ReadConsumptionResponse.builder()
                        .cardId(cardId)
                        .categoryName(category.getCardCategoryName())
                        .totalAmount(totalAmount)
                        .amount(categoryAmountMap.getOrDefault(category.getCardCategoryName(), 0))
                        .build())
                .collect(Collectors.toList());
    }

}
