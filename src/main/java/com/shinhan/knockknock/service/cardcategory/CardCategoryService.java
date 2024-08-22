package com.shinhan.knockknock.service.cardcategory;

import com.shinhan.knockknock.domain.dto.cardcategory.CreateCardCategoryRequest;
import com.shinhan.knockknock.domain.dto.cardcategory.ReadCardCategoryResponse;
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
                .cardCategoryName(entity.getCardCategoryName())
                .build();
        return response;
    }
}
