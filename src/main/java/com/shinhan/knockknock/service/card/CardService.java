package com.shinhan.knockknock.service.card;

import com.shinhan.knockknock.domain.dto.card.CreateCardIssueResponse;
import com.shinhan.knockknock.domain.dto.card.ReadCardResponse;
import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.domain.entity.CardIssueEntity;

import java.util.List;

public interface CardService {

    // 비동기 카드 발급 수헹
    public void scheduleCreatePostCard(CardIssueEntity cardIssueEntity, String password);

    // 카드 발급
    public CreateCardIssueResponse createPostCard(CardIssueEntity cardIssueEntity, String password);

    // 본인 카드 조회
    public List<ReadCardResponse> readGetCards(Long userNo);

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
                .cardAddress(cardEntity.getCardAddress())
                .cardIsFamily(cardEntity.isCardIsfamily())
                .build();
        return readCardResponse;
    }

}
