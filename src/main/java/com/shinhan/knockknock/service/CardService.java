package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateCardResponse;
import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.domain.entity.CardIssueEntity;

public interface CardService {

    // 카드 발급
    public CardEntity createCard(CardIssueEntity cardIssueEntity);

    // Entity -> DTO
    default CreateCardResponse transformEntityToDTO(CardEntity cardEntity){
        CreateCardResponse createCardResponse = CreateCardResponse
                .builder()
                .cardNo(cardEntity.getCardNo())
                .cardCvc(cardEntity.getCardCvc())
                .cardEname(cardEntity.getCardEname())
                .cardBank(cardEntity.getCardBank())
                .cardAccount(cardEntity.getCardAccount())
                .cardAmountDate(cardEntity.getCardAmountDate())
                .cardExpiredate(cardEntity.getCardExpiredate())
                .build();
        return createCardResponse;
    }
}
