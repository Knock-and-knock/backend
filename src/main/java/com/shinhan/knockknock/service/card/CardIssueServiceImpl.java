package com.shinhan.knockknock.service.card;

import com.shinhan.knockknock.domain.dto.card.CreateCardIssueRequest;
import com.shinhan.knockknock.domain.dto.card.CreateCardIssueResponse;
import com.shinhan.knockknock.domain.dto.card.ReadCardIssueResponse;
import com.shinhan.knockknock.domain.dto.card.ReadMemberInfo;
import com.shinhan.knockknock.domain.entity.CardIssueEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.exception.NoCardIssueFoundException;
import com.shinhan.knockknock.repository.CardIssueRepository;
import com.shinhan.knockknock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardIssueServiceImpl implements CardIssueService {

    @Autowired
    CardIssueRepository cardIssueRepository;
    @Autowired
    CardService cardService;
    @Autowired
    UserRepository userRepository;

    // 기본 정보 조회
    public ReadMemberInfo readMemberInfo(Long userNo){
        UserEntity userEntity = userRepository.findByUserNo(userNo);
        ReadMemberInfo readMemberInfo = ReadMemberInfo
                .builder()
                .userName(userEntity.getUserName())
                .userPhone(userEntity.getUserPhone())
                .build();
        return readMemberInfo;
    }

    // 카드 발급
    @Override
    public CreateCardIssueResponse createPostCardIssue(CreateCardIssueRequest request, Long userNo) {

        // 요청 검증
        //request.validate();

        // CardIssueEntity에 token에서 가져온 userNo 붙이고 생성
        CardIssueEntity cardIssueEntity = transformDTOToEntity(request);
        cardIssueEntity.setUserNo(userNo);
        cardIssueRepository.save(cardIssueEntity);

        // 1분 후에 카드 발급 수행
        cardService.scheduleCreatePostCard(cardIssueEntity, request.getCardIssuePassword()
                , request.getCardIssueKname(), request.getCardIssuePhone());

        return CreateCardIssueResponse.builder()
                .message("카드 발급 요청이 접수되었습니다.")
                .status(HttpStatus.ACCEPTED)
                .build();
    }

    // 추후 가족 카드 발급 시 신청정보 조회
    public ReadCardIssueResponse readLatestIssueInfo(Long userNo) {
        CardIssueEntity cardIssueEntity = cardIssueRepository.findFirstByUserNoOrderByCardIssueIssueDateDesc(userNo)
                .orElseThrow(() -> new NoCardIssueFoundException("최근 발급된 카드 신청 정보가 없습니다. 개인카드를 발급해주세요."));
        cardIssueEntity.setCardIssueIsFamily(true);
        return transformEntityToDTO(cardIssueEntity);
    }



    @Override
    public String mergeName(CreateCardIssueRequest request){
        return request.getCardIssueEname() != null
                ? request.getCardIssueEname() 
                : request.getCardIssueFirstEname() + " " + request.getCardIssueLastEname();
    }

    @Override
    public String mergeAddress(CreateCardIssueRequest request){
        return request.getCardIssueAddress() != null
                ? request.getCardIssueFirstAddress()
                : request.getCardIssueFirstAddress() + " " + request.getCardIssueSecondAddress();
    }
}
