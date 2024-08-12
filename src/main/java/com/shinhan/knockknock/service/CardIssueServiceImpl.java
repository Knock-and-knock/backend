package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateCardIssueRequest;
import com.shinhan.knockknock.domain.dto.CreateCardResponse;
import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.domain.entity.CardIssueEntity;
import com.shinhan.knockknock.repository.CardIssueRepository;
import com.shinhan.knockknock.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardIssueServiceImpl implements CardIssueService {

    @Autowired
    CardIssueRepository cardIssueRepository;
    @Autowired
    CardService cardService;

    @Override
    public CreateCardResponse createPostCardIssue(CreateCardIssueRequest request) {
        // CardIssueEntity 생성
        CardIssueEntity cardIssueEntity = cardIssueRepository.save(transformDTOToEntity(request));

        // CardEntity 생성
        CardEntity cardEntity = cardService.createCard(cardIssueEntity);

        // CardEntity -> CreateCardResponse
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
