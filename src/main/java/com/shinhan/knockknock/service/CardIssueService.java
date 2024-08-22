package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateCardIssueRequest;
import com.shinhan.knockknock.domain.dto.CreateCardIssueResponse;
import com.shinhan.knockknock.domain.entity.CardIssueEntity;
public interface CardIssueService {

    // 카드 발급 요청
    public CreateCardIssueResponse createPostCardIssue(CreateCardIssueRequest request, Long userNo);

    // 영문 이름 처리
    public String mergeName(CreateCardIssueRequest cardIssueEntity);

    // DTO -> Entity
    default CardIssueEntity transformDTOToEntity(CreateCardIssueRequest request){

        CardIssueEntity cardIssueEntity = CardIssueEntity.builder()
                //.cardIssueResidentNo(request.getCardIssueResidentNo())
                .cardIssueEname(request.getCardIssueEname())
                .cardIssueEmail(request.getCardIssueEmail())
                .cardIssueBank(request.getCardIssueBank())
                .cardIssueAccount(request.getCardIssueAccount())
                .cardIssueIsAgree(request.isCardIssueIsAgree())
                .cardIssueIncome(request.getCardIssueIncome())
                .cardIssueCredit(request.getCardIssueCredit())
                .cardIssueAmountDate(request.getCardIssueAmountDate())
                .cardIssueSource(request.getCardIssueSource())
                .cardIssueIsHighrisk(request.getCardIssueIsHighrisk())
                .cardIssuePurpose(request.getCardIssuePurpose())
                .cardIssueIsFamily(request.isCardIssueIsFamily())
                .cardIssueAddress(request.getCardIssueAddress())
                .build();

        return cardIssueEntity;
    }
}
