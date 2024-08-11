package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateCardIssueRequest;
import com.shinhan.knockknock.domain.dto.CreateCardResponse;
import com.shinhan.knockknock.domain.entity.CardIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardIssueService {

    // 카드 발급 요청
    public CreateCardResponse createPostCardIssue(CreateCardIssueRequest request);

    // DTO -> Entity
    default CardIssueEntity transformDTOToEntity(CreateCardIssueRequest request){

        CardIssueEntity cardIssueEntity = CardIssueEntity.builder()
                .cardIssueResidentNo(request.getCardIssueResidentNo())
                .cardIssueEname(request.getCardIssueEname())
                .cardIssueEmail(request.getCardIssueEmail())
                .cardIssueBank(request.getCardIssueBank())
                .cardIssueAccount(request.getCardIssueAccount())
                .cardIssueIsAgree(request.isCardIssueIsAgree())
                .cardIssueIncome(request.getCardIssueIncome())
                .cardIssueCredit(request.getCardIssueCredit())
                .cardIssueAmountDate(request.getCardIssueAmountDate())
                .cardIssueSource(request.getCardIssueSource())
                .cardIssueIsHighrisk(request.isCardIssueIsHighrisk())
                .cardIssuePurpose(request.getCardIssuePurpose())
                .userNo(request.getUserNo())
                .build();

        return cardIssueEntity;
    }
}
