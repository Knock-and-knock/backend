package com.shinhan.knockknock.service.consumption;

import com.shinhan.knockknock.domain.dto.consumption.ReadConsumptionResponse;
import com.shinhan.knockknock.domain.entity.CardCategoryEntity;
import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.domain.entity.CardHistoryEntity;
import com.shinhan.knockknock.repository.CardCategoryRepository;
import com.shinhan.knockknock.repository.CardRepository;
import com.shinhan.knockknock.repository.ConsumptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    @Override
    public List<CardEntity> readCardByUserNo(Long userNo) {
        return cardRepository.findCardByUserNo(userNo);
    }

    /**
     * 주어진 사용자 번호와 날짜 범위를 기반으로 월별 소비 리포트를 생성
     *
     * @param userNo    - 사용자 번호
     * @param startDate - 검색 기준 시작일
     * @param endDate   - 검색 기준 마감일
     * @return {
     * "cardId": 1,
     * "categoryName": "교통",
     * "totalAmount": 218000,
     * "amount": 25000,
     * "family": false
     * },
     */
    public List<ReadConsumptionResponse> readConsumptionReportList(Long userNo, Date startDate, Date endDate) {
        // 사용자 번호를 기반으로 카드 번호 리스트 조회
        List<String> cardNos = readCardNoByUserNo(userNo);

        // 카드 번호 리스트와 날짜 범위를 기반으로 카드 이력 조회
        List<CardHistoryEntity> cardHistories = consumptionRepository
                .findCardHistoriesByCard_CardNoInAndCardHistoryApproveBetween(cardNos, startDate, endDate);

        // 카테고리별, 카드 번호별로 금액을 합산하여 Map으로 저장
        Map<String, Map<String, Integer>> categorySumMap = cardHistories.stream()
                .filter(cardHistory -> cardHistory.getCardCategory() != null) // null 체크 추가
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
                                .categoryName(categoryEntry.getKey())  // 카테고리 이름 설정
                                .amount(cardEntry.getValue())  // 각 카드의 소비 금액 설정
                                .totalAmount(totalAmount)  // 전체 소비 금액 설정
                                .cardId(cardHistories.stream()
                                        .filter(ch -> ch.getCard().getCardNo().equals(cardEntry.getKey()))
                                        .findFirst()
                                        .map(ch -> ch.getCard().getCardId())
                                        .orElse(null))  // 카드 ID 설정
                                .build()
                        )
                ).collect(Collectors.toList());
    }


    @Override
    public String readConsumptionReportForConversation(CardEntity card, String date) {
        // 입력된 날짜 문자열 ("2024-08")을 Date 객체로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        java.util.Date parsedDate;
        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format is yyyy-MM", e);
        }

        // Calendar 객체를 생성하고 파싱된 날짜를 설정
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsedDate);

        // 월의 첫날 설정
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        java.sql.Date startDate = new java.sql.Date(calendar.getTimeInMillis());

        // 월의 마지막 날 설정
        calendar.add(Calendar.MONTH, 1);  // 다음 달로 이동
        calendar.set(Calendar.DAY_OF_MONTH, 1);  // 다음 달의 첫 날로 설정
        calendar.add(Calendar.DATE, -1);  // 하루를 빼서 이번 달의 마지막 날로 설정
        java.sql.Date endDate = new java.sql.Date(calendar.getTimeInMillis());

        // 계산된 날짜를 사용하여 리포트 생성
        List<ReadConsumptionResponse> consumptionReport = readConsumptionReport(card.getCardId(), startDate, endDate);

        // 문자열 포맷을 위한 SimpleDateFormat 생성
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 총 소비액을 계산 (totalAmount는 같은 값을 가진다고 가정)
        int totalAmount = consumptionReport.isEmpty() ? 0 : consumptionReport.get(0).getTotalAmount();

        // 최종 문자열 빌더
        StringBuilder reportBuilder = new StringBuilder();

        // 시작 날짜, 끝 날짜, 카드 ID, 총 소비액 추가
        reportBuilder.append("Start Date: ").append(outputDateFormat.format(startDate))
                .append(", End Date: ").append(outputDateFormat.format(endDate))
                .append(", Card Number: ").append(card.getCardNo().split("-")[0])
                .append(", Total Consumption: ").append(totalAmount)
                .append("\nCategory-wise Consumption:\n");

        // 각 카테고리별 소비 내역 및 백분율 추가
        for (ReadConsumptionResponse response : consumptionReport) {
            int amount = response.getAmount();
            double percentage = (totalAmount > 0) ? ((double) amount / totalAmount) * 100 : 0;

            reportBuilder.append(response.getCategoryName())
                    .append(": ")
                    .append(amount)
                    .append(" (")
                    .append(String.format("%.2f", percentage))
                    .append("%)\n");
        }

        return reportBuilder.toString();
    }

    /**
     * 주어진 cardId와 startDate, endDate로 소비 리포트를 생성
     *
     * @param cardId    - 카드 식별번호
     * @param startDate - 검색 기준 시작일
     * @param endDate   - 검색 기준 마감일
     * @return List<ReadConsumptionResponse>
     */
    public List<ReadConsumptionResponse> readConsumptionReport(Long cardId, java.sql.Date startDate, java.sql.Date endDate) {
        // 모든 카테고리를 조회
        List<CardCategoryEntity> categories = cardCategoryRepository.findAll();

        // 카드 ID와 날짜 범위로 소비 내역 조회
        List<CardHistoryEntity> cardHistories = consumptionRepository.findCardHistoriesByCardIdAndDateRange(cardId, startDate, endDate);

        // 카테고리별 금액 합산
        Map<String, Integer> categoryAmountMap = cardHistories.stream()
                .filter(cardHistory -> cardHistory.getCardCategory() != null) // null 체크 추가
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
