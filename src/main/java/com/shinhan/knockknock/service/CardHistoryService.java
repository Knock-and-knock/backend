package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateCardHistoryRequest;
import com.shinhan.knockknock.domain.dto.ReadCardHistoryResponse;
import com.shinhan.knockknock.domain.entity.CardHistoryEntity;

import java.util.List;

public interface CardHistoryService {
    //Create
    Long createCardHistory(CreateCardHistoryRequest request);

    //Read
    List<ReadCardHistoryResponse> readAll();

    //Dto -> Entity
    default CardHistoryEntity dtoToEntity(CreateCardHistoryRequest request){
        CardHistoryEntity entity = CardHistoryEntity.builder()
                .cardHistoryNo(request.getCardHistoryNo())
                .cardHistoryAmount(request.getCardHistoryAmount())
                .cardHistoryShopname(request.getCardHistoryShopname())
                .cardHistoryApprove(request.getCardHistoryApprove())
                .cardCategoryNo(request.getCardCategoryNo())
                .cardId(request.getCardId())
                .build();
        return entity;
    }

    //Entity -> Dto
    default ReadCardHistoryResponse entityToDto(CardHistoryEntity entity){
        ReadCardHistoryResponse response = ReadCardHistoryResponse.builder()
                .cardHistoryNo(entity.getCardHistoryNo())
                .cardHistoryAmount(entity.getCardHistoryAmount())
                .cardHistoryShopname(entity.getCardHistoryShopname())
                .cardHistoryApprove(entity.getCardHistoryApprove())
                .cardCategoryNo(entity.getCardCategoryNo())
                .cardId(entity.getCardId())
                .build();
        return response;
    }
}
