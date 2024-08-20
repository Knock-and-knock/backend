package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateCardHistoryRequest;
import com.shinhan.knockknock.domain.dto.ReadCardHistoryResponse;
import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.domain.entity.CardHistoryEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface CardHistoryService {
    //Create
    Long createCardHistory(CreateCardHistoryRequest request);

    //Read
    List<ReadCardHistoryResponse> readAll();

    //Dto -> Entity
    default CardHistoryEntity dtoToEntity(CreateCardHistoryRequest request) {
        return CardHistoryEntity.builder()
                .cardHistoryNo(request.getCardHistoryNo())
                .cardHistoryAmount(request.getCardHistoryAmount())
                .cardHistoryShopname(request.getCardHistoryShopname())
                .cardHistoryApprove(request.getCardHistoryApprove())
                .cardCategoryNo(request.getCardCategoryNo())
                .cardId(request.getCardId())
                .build();
    }

    //Entity -> Dto
    default ReadCardHistoryResponse entityToDto(CardHistoryEntity entity, UserEntity user) {
        String userName = user.getUserName();

        // 카드가 가족 카드인 경우, 관련된 사용자의 이름을 찾음
        if (entity.getCardId().isCardIsfamily()) {
            // 카드 은행과 계좌를 기준으로 관련된 사용자를 찾는 메서드가 있다고 가정
            userName = findUserNameForFamilyCard(entity.getCardId());
        }

        return ReadCardHistoryResponse.builder()
                .cardHistoryAmount(entity.getCardHistoryAmount())
                .cardHistoryShopname(entity.getCardHistoryShopname())
                .cardHistoryApprove(entity.getCardHistoryApprove())
                .cardCategoryNo(entity.getCardCategoryNo())
                .cardId(entity.getCardId())
                .userName(userName)  // 사용자의 이름을 추가
                .build();
    }

    // 가족 카드일 경우 관련된 사용자의 이름을 찾는 메서드
    default String findUserNameForFamilyCard(@NotNull CardEntity card) {
        // 관련된 사용자를 찾는 로직을 구현
        // 동일한 은행과 계좌를 가진 사용자를 데이터베이스에서 조회하는 로직이 필요함
        return "Related User Name";  // 실제 구현으로 대체
    }
}
