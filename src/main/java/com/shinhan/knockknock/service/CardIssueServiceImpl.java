package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateCardIssueRequest;
import com.shinhan.knockknock.domain.dto.CreateCardIssueResponse;
import com.shinhan.knockknock.domain.entity.CardIssueEntity;
import com.shinhan.knockknock.repository.CardIssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CardIssueServiceImpl implements CardIssueService {

    @Autowired
    CardIssueRepository cardIssueRepository;
    @Autowired
    CardService cardService;

    @Override
    public CreateCardIssueResponse createPostCardIssue(CreateCardIssueRequest request, Long userNo) {

        String password = request.getCardIssuePassword();

        // CardIssueEntity에 token에서 가져온 userNo 붙이고 생성
        CardIssueEntity cardIssueEntity = transformDTOToEntity(request);
        cardIssueEntity.setUserNo(userNo);
        cardIssueRepository.save(cardIssueEntity);

        // 1분 후에 카드 발급 수행
        cardService.scheduleCreatePostCard(cardIssueEntity, password);

        return CreateCardIssueResponse.builder()
                .message("카드 발급 요청이 접수되었습니다.")
                .status(HttpStatus.ACCEPTED)
                .build();
    }
}
