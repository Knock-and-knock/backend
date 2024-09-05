package com.shinhan.knockknock.service.card;

import com.shinhan.knockknock.domain.dto.card.CreateCardIssueResponse;
import com.shinhan.knockknock.domain.dto.card.ReadCardResponse;
import com.shinhan.knockknock.domain.dto.card.ReadIsCardResponse;
import com.shinhan.knockknock.domain.entity.*;
import com.shinhan.knockknock.repository.*;
import com.shinhan.knockknock.service.notification.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    CardIssueRepository cardIssueRepository;
    @Autowired
    NotificationServiceImpl notificationService;
    @Autowired
    ConsumptionRepository consumptionRepository;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MatchRepository matchRepository;

    @Async("taskExecutor")
    public void scheduleCreatePostCard(CardIssueEntity cardIssueEntity, String password
            , String cardIssueKname, String cardIssuePhone) {
        scheduler.schedule(() -> createPostCard(cardIssueEntity, password, cardIssueKname, cardIssuePhone)
                , 10, TimeUnit.SECONDS);
    }

    // 카드 발급
    @Override
    public CreateCardIssueResponse createPostCard(CardIssueEntity cardIssueEntity, String password
            , String cardIssueKname, String cardIssuePhone) {
        Random random = new Random();
        String phoneNum = cardIssuePhone;

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

        // 결제일 처리 // firstday, middleday, lastday
        String amountDate = cardIssueEntity.getCardIssueAmountDate();
        if(amountDate.equals("firstday")){amountDate="01";}
        else if (amountDate.equals("middleday")){amountDate="15";}
        else {amountDate="30";}

        if (cardIssueKname == null || cardIssueKname.isEmpty()) {
            cardIssueKname = null;
        }

        if (cardIssuePhone == null || cardIssuePhone.isEmpty()) {
            phoneNum = null;
        } else {
            // 전화번호 user_tb랑 형식 맞추기
            phoneNum = phoneNum.replaceAll("-", "");
        }

        System.out.println("cardUserKname: " + cardIssueKname);
        System.out.println("cardUserPhone: " + phoneNum);

        CardEntity cardEntity = CardEntity.builder()
                .cardNo(cardNo)
                .cardCvc(formattedCvc)
                .cardEname(cardIssueEntity.getCardIssueEname())
                .cardPassword(password)
                .cardBank(cardIssueEntity.getCardIssueBank())
                .cardAccount(cardIssueEntity.getCardIssueAccount())
                .cardAmountDate(amountDate)
                .cardExpiredate(expireDate)
                .cardIssueNo(cardIssueEntity.getCardIssueNo())
                .userNo(cardIssueEntity.getUserNo())
                .cardIsfamily(cardIssueEntity.isCardIssueIsFamily())
                .cardAddress(cardIssueEntity.getCardIssueAddress())
                .cardUserKname(cardIssueKname)
                .cardUserPhone(phoneNum)
                .build();

        // 카드 발급
        cardRepository.save(cardEntity);

        // 알림 서비스 수행
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .notificationCategory("카드 발급")
                .notificationTitle("카드 발급 완료")
                .notificationContent("회원님의 카드 발급이 완료되었습니다.")
                .userNo(cardIssueEntity.getUserNo())
                .build();
        notificationService.notify(notificationEntity);

        // 응답
        CreateCardIssueResponse createCardIssueResponse = CreateCardIssueResponse
                .builder()
                .message("카드 발급 성공")
                .status(HttpStatus.CREATED)
                .build();

        return createCardIssueResponse;
    }

    // 본인 카드 리스트 조회
    // userNo로 CardEntity 리스트를 조회한 후, 각 카드에 대해 현재 월의 총 소비 금액을 계산하여 ReadCardResponse 객체에 포함
    // 사용자와 이름과 전화번호가 일치하는 카드 조회
    @Override
    public List<ReadCardResponse> readGetCards(Long userNo) {
        int countCardIssue = 0;
        String userName = String.valueOf(userRepository.findByUserNo(userNo).getUserName()); // 이름
        String userPhone = String.valueOf(userRepository.findByUserNo(userNo).getUserPhone()); // 전화번호

        // userNo로 카드 리스트 조회 ( 본인이 발급한 모든 카드 )
        List<CardEntity> cardEntities = cardRepository.findByUserNo(userNo);

        // 이름 + 전화번호로 카드 리스트 조회 ( 다른 사람이 자신이 사용하도록 만든 가족카드 조회 )
        List<CardEntity> cardEntitiesByNameAndPhone = cardRepository.findByCardUserKnameAndCardUserPhone(userName, userPhone);
        cardEntities.addAll(cardEntitiesByNameAndPhone);

        // 중복 제거
        Set<Long> seenCardIds = new HashSet<>();
        List<CardEntity> uniqueCardEntities = Stream.concat(cardEntities.stream(), cardEntitiesByNameAndPhone.stream())
                .filter(card -> seenCardIds.add(card.getCardId()))
                .toList();

        if(cardEntities.isEmpty()){ // 1. if 발급된 카드가 없다
            countCardIssue = cardIssueRepository.countByUserNo(userNo);
            if (countCardIssue == 0){ // 2. if CardIssue 테이블에 신청 정보가 없다 -> 발급된 카드가 없습니다.
                ReadCardResponse readCardResponse = new ReadCardResponse();
                readCardResponse.setCardResponseMessage("발급된 카드가 없습니다.");
                return Collections.singletonList(readCardResponse); // 단건 메시지 응답
            } else { // 3. CardIssue 테이블에 신청 정보가 있다 -> 발급 대기중입니다.
                ReadCardResponse response = new ReadCardResponse();
                response.setCardResponseMessage("카드 발급이 대기 중입니다.");
                return Collections.singletonList(response); // 단건 메시지 응답
            }

        } else {
            List<ReadCardResponse> readCardResponses = cardEntities.stream()
                    .map(cardEntity -> {
                        Long cardId = cardEntity.getCardId();

                        // 이번 달 카드별 사용 금액 계산
                        YearMonth currentMonth = YearMonth.now();
                        LocalDateTime startDate = currentMonth.atDay(1).atStartOfDay();
                        LocalDateTime endDate = currentMonth.atEndOfMonth().atTime(23, 59, 59);
                        Long totalAmount = consumptionRepository.findTotalAmountByCardIdAndCurrentMonth(cardId, startDate, endDate);

                        if (totalAmount == null) {
                            totalAmount = 0L;
                        }

                        return ReadCardResponse.builder()
                                .cardId(cardEntity.getCardId())
                                .cardNo(cardEntity.getCardNo())
                                .cardBank(cardEntity.getCardBank())
                                .cardIsFamily(cardEntity.isCardIsfamily())
                                .totalAmount(totalAmount)
                                .cardResponseMessage("카드가 정상적으로 조회되었습니다.")
                                .build();
                    })
                    .collect(Collectors.toList());

            return readCardResponses;
        }
    }

    private Long getProtectorUserNo(Long userNo) {
        // 피보호자 유저 번호로 매칭 정보를 가져옴
        Optional<MatchEntity> matchEntityOptional = matchRepository.findByUserProtege_UserNo(userNo);

        // 매칭 정보가 존재하는 경우 보호자의 UserNo 반환, 존재하지 않으면 null 반환
        return matchEntityOptional
                .map(matchEntity -> matchEntity.getUserProtector().getUserNo())
                .orElse(null);
    }

    // 카드 테이블에서 userNo의 카드가 존재하는지 여부
    public ReadIsCardResponse readIsCard(Long userNo) {
        boolean isCard = false;
        Long count = 0L;
        // 본인이 발급한 카드 개수 조회
        count = cardRepository.countByUserNo(userNo);

        // 본인이 사용할 수 있는(이름 + 전화번호) 가족카드 개수 조회
        UserEntity user = userRepository.findByUserNo(userNo);
        count += cardRepository.countByUserNameAndUserPhone(user.getUserName(), user.getUserPhone());

        if (count != 0){ isCard = true; }

        ReadIsCardResponse readIsCardResponse = ReadIsCardResponse
                .builder()
                .isCard(isCard)
                .build();
        return readIsCardResponse;
    }

}
