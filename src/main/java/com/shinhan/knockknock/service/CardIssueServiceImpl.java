package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateCardIssueRequest;
import com.shinhan.knockknock.domain.dto.CreateCardIssueResponse;
import com.shinhan.knockknock.domain.entity.CardIssueEntity;
import com.shinhan.knockknock.repository.CardIssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardIssueServiceImpl implements CardIssueService {

    @Autowired
    CardIssueRepository cardIssueRepository;
    @Autowired
    CardService cardService;

    @Override
    public CreateCardIssueResponse createPostCardIssue(CreateCardIssueRequest request) {
        // CardIssueEntity 생성
        CardIssueEntity cardIssueEntity = cardIssueRepository.save(transformDTOToEntity(request));

        // CardEntity 생성
        CreateCardIssueResponse createCardIssueResponse = cardService.createCard(cardIssueEntity);

        return createCardIssueResponse;
    }
}
