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
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    CardRepository cardRepository;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Async("taskExecutor")
    public void scheduleCreateCard(CardIssueEntity cardIssueEntity) {
        scheduler.schedule(() -> createCard(cardIssueEntity), 1, TimeUnit.MINUTES);
    }

    @Override
    public CreateCardIssueResponse createCard(CardIssueEntity cardIssueEntity) {
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
                .cardPassword(1234)
                .cardBank(cardIssueEntity.getCardIssueBank())
                .cardAccount(cardIssueEntity.getCardIssueAccount())
                .cardAmountDate(cardIssueEntity.getCardIssueAmountDate())
                .cardExpiredate(expireDate)
                .cardIssueNo(cardIssueEntity.getCardIssueNo())
                .userNo(cardIssueEntity.getUserNo())
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

    // 카드 조회
    @Override
    public ReadCardResponse readGetCard(Long userNo) {
        CardEntity cardEntity = cardRepository.findById(userNo).orElse(null);
        ReadCardResponse readCardResponse = transformEntityToDTO(cardEntity);

        // 만료 일자 형식 변환
        String cardExpireDate = readCardResponse.getCardExpiredate();
        String date = cardExpireDate.substring(2,7);
        date = date.replace("-", "/");
        readCardResponse.setCardExpiredate(date);

        return readCardResponse;
    }

}
