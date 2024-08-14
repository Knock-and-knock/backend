package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateCardIssueResponse;
import com.shinhan.knockknock.domain.dto.ReadCardResponse;
import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.domain.entity.CardIssueEntity;

public interface CardService {

    // 비동기 카드 발급 수헹
    public void scheduleCreateCard(CardIssueEntity cardIssueEntity);

    // 카드 발급
    public CreateCardIssueResponse createCard(CardIssueEntity cardIssueEntity);

    // 카드 조회
    public ReadCardResponse readGetCard(Long userNo);

    // Entity -> DTO
    default ReadCardResponse transformEntityToDTO(CardEntity cardEntity){
        ReadCardResponse readCardResponse = ReadCardResponse
                .builder()
                .cardNo(cardEntity.getCardNo())
                .cardCvc(cardEntity.getCardCvc())
                .cardEname(cardEntity.getCardEname())
                .cardBank(cardEntity.getCardBank())
                .cardAccount(cardEntity.getCardAccount())
                .cardAmountDate(cardEntity.getCardAmountDate())
                .cardExpiredate(cardEntity.getCardExpiredate().toString())
                .build();
        return readCardResponse;
    }
}
