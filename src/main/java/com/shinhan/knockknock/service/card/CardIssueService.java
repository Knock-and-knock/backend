package com.shinhan.knockknock.service.card;

import com.shinhan.knockknock.domain.dto.card.CreateCardIssueRequest;
import com.shinhan.knockknock.domain.dto.card.CreateCardIssueResponse;
import com.shinhan.knockknock.domain.dto.card.ReadCardIssueResponse;
import com.shinhan.knockknock.domain.dto.card.ReadMemberInfo;
import com.shinhan.knockknock.domain.entity.CardIssueEntity;
import com.shinhan.knockknock.domain.entity.RiskEnum;

import java.sql.Date;
import java.util.List;

public interface CardIssueService {

    // 기본 정보 조회
    public ReadMemberInfo readMemberInfo(Long userNo);

    // 카드 발급 요청
    public CreateCardIssueResponse createPostCardIssue(CreateCardIssueRequest request, Long userNo);

    // 가장 최근 개인 카드 신청 정보 조회
    public ReadCardIssueResponse readLatestIssueInfo(Long userNo);

    // 영문 이름 처리
    public String mergeName(CreateCardIssueRequest createCardIssueRequest);

    // 주소 처리
    public String mergeAddress(CreateCardIssueRequest createCardIssueRequest);

    // DTO -> Entity
    default CardIssueEntity transformDTOToEntity(CreateCardIssueRequest request){

        CardIssueEntity cardIssueEntity = CardIssueEntity.builder()
                .cardIssueEname(request.getCardIssueEname())
                .cardIssueEmail(request.getCardIssueEmail())
                .cardIssueBank(request.getCardIssueBank())
                .cardIssueAccount(request.getCardIssueAccount())
                .cardIssueIsAgree(request.isCardIssueIsAgree())
                .cardIssueIncome(request.getCardIssueIncome())
                .cardIssueCredit(request.getCardIssueCredit())
                .cardIssueAmountDate(request.getCardIssueAmountDate())
                .cardIssueSource(request.getCardIssueSource())
                .cardIssueIsHighrisk(RiskEnum.valueOf(String.valueOf(request.getCardIssueIsHighrisk())))
                .cardIssuePurpose(request.getCardIssuePurpose())
                .cardIssueIsFamily(request.isCardIssueIsFamily())
                .cardIssueAddress(request.getCardIssueAddress())
                .build();

        return cardIssueEntity;
    }

    default ReadCardIssueResponse transformEntityToDTO(CardIssueEntity cardIssueEntity){
        ReadCardIssueResponse readCardIssueResponse = ReadCardIssueResponse.builder()
                .cardIssueEmail(cardIssueEntity.getCardIssueEmail())
                .cardIssueBank(cardIssueEntity.getCardIssueBank())
                .cardIssueAccount(cardIssueEntity.getCardIssueAccount())
                .cardIssueIsAgree(cardIssueEntity.isCardIssueIsAgree())
                .cardIssueIncome(cardIssueEntity.getCardIssueIncome())
                .cardIssueCredit(cardIssueEntity.getCardIssueCredit())
                .cardIssueAmountDate(String.valueOf(cardIssueEntity.getCardIssueAmountDate()))
                .cardIssueSource(cardIssueEntity.getCardIssueSource())
                .cardIssueIsHighrisk(cardIssueEntity.getCardIssueIsHighrisk())
                .cardIssuePurpose(cardIssueEntity.getCardIssuePurpose())
                .cardIssueIsFamily(String.valueOf(cardIssueEntity.isCardIssueIsFamily()))
                .build();
        return readCardIssueResponse;
    }
}
