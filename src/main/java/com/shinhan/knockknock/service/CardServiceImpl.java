package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateCardIssueResponse;
import com.shinhan.knockknock.domain.dto.ReadCardResponse;
import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.domain.entity.CardIssueEntity;
import com.shinhan.knockknock.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    CardRepository cardRepository;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Async("taskExecutor")
    public void scheduleCreatePostCard(CardIssueEntity cardIssueEntity, String password) {
        scheduler.schedule(() -> createPostCard(cardIssueEntity, password), 1, TimeUnit.MINUTES);
    }

    @Override
    public CreateCardIssueResponse createPostCard(CardIssueEntity cardIssueEntity, String password) {
        Random random = new Random();

        // 카드번호 생성
        String[] randomNumbers = new String[4];
        for (int i = 0; i < 4; i++) {
            int randomNumber = random.nextInt(10000);
            randomNumbers[i] = String.format("%04d", randomNumber); // 4자리 형식
        }
        String cardNo = String.join("-", randomNumbers);

        // CVC 생성
        int cvc = random.nextInt(1000);
        String formattedCvc = String.format("%03d", cvc); // 3자리 형식

        // 카드 만료 일자 생성
        Date todayDate = new Date(System.currentTimeMillis());
        LocalDate localDate = todayDate.toLocalDate();
        LocalDate newLocalDate = localDate.plusYears(5);
        Date expireDate = Date.valueOf(newLocalDate);

        CardEntity cardEntity = CardEntity.builder()
                .cardNo(cardNo)
                .cardCvc(formattedCvc)
                .cardEname(cardIssueEntity.getCardIssueEname())
                .cardPassword(password)
                .cardBank(cardIssueEntity.getCardIssueBank())
                .cardAccount(cardIssueEntity.getCardIssueAccount())
                .cardAmountDate(cardIssueEntity.getCardIssueAmountDate())
                .cardExpiredate(expireDate)
                .cardIssueNo(cardIssueEntity.getCardIssueNo())
                .userNo(cardIssueEntity.getUserNo())
                .cardIsfamily(cardIssueEntity.isCardIssueIsFamily())
                .cardAddress(cardIssueEntity.getCardIssueAddress())
                .build();

        // 카드 발급
        cardRepository.save(cardEntity);

        // 응답
        CreateCardIssueResponse createCardIssueResponse = CreateCardIssueResponse
                .builder()
                .message("카드 발급 성공")
                .status(HttpStatus.CREATED)
                .build();

        return createCardIssueResponse;
    }

    // 본인 카드 조회
    @Override
    public List<ReadCardResponse> readGetCards(Long userNo) {
        // 여러 ID에 해당하는 CardEntity 목록 조회
        List<CardEntity> cardEntities = cardRepository.findByUserNo(userNo);

        // 각 CardEntity를 ReadCardResponse로 변환
        List<ReadCardResponse> readCardResponses = cardEntities.stream()
                .map(this::transformEntityToDTO)
                .collect(Collectors.toList());

        // 만료 일자 형식 변환
        readCardResponses.forEach(readCardResponse -> {
            String cardExpireDate = readCardResponse.getCardExpiredate();
            String date = cardExpireDate.substring(2,7);
            date = date.replace("-", "/");
            readCardResponse.setCardExpiredate(date);
        });

        return readCardResponses;
    }


}
