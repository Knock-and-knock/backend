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

    // 본인 카드 리스트 조회
    public List<ReadCardResponse> readGetCards(Long userNo);

    // Entity -> DTO
    default ReadCardResponse transformEntityToDTO(CardEntity cardEntity){
        ReadCardResponse readCardResponse = ReadCardResponse
                .builder()
                .cardId(cardEntity.getCardId())
                .cardNo(cardEntity.getCardNo())
                .cardIsFamily(cardEntity.isCardIsfamily())
                .build();
        return readCardResponse;
    }

}
