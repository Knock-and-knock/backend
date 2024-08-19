package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateCardCategoryRequest;
import com.shinhan.knockknock.domain.dto.ReadCardCategoryResponse;
import com.shinhan.knockknock.domain.entity.CardCategoryEntity;

import java.util.List;

public interface CardCategoryService {

    //Create
    Long createCardCategory(CreateCardCategoryRequest request);

    //Read
    List<ReadCardCategoryResponse> readAll();

    //Update
    void updateCardCategory(CreateCardCategoryRequest request);

    //Dto -> Entity
    default CardCategoryEntity dtoToEntity(CreateCardCategoryRequest request){
        CardCategoryEntity entity = CardCategoryEntity.builder()
                .cardCategoryNo(request.getCardCategoryNo())
                .cardCategoryName(request.getCardCategoryName())
                .build();
        return entity;
    }

    //Entity -> Dto
    default ReadCardCategoryResponse entityToDto(CardCategoryEntity entity){
        ReadCardCategoryResponse response = ReadCardCategoryResponse.builder()
                .cardCategoryNo(entity.getCardCategoryNo())
                .cardCategoryName(entity.getCardCategoryName())
                .build();
        return response;
    }
}
