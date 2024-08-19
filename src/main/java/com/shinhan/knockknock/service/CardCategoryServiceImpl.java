package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateCardCategoryRequest;
import com.shinhan.knockknock.domain.dto.ReadCardCategoryResponse;
import com.shinhan.knockknock.domain.entity.CardCategoryEntity;
import com.shinhan.knockknock.repository.CardCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CardCategoryServiceImpl implements CardCategoryService{

    @Autowired
    CardCategoryRepository cardCategoryRepository;

    @Override
    public Long createCardCategory(CreateCardCategoryRequest request) {
        CardCategoryEntity newCardCategory = cardCategoryRepository.save(dtoToEntity(request));
        return null;
    }

    @Override
    public List<ReadCardCategoryResponse> readAll() {
        List<CardCategoryEntity> entityList = cardCategoryRepository.findAll();
        Function<CardCategoryEntity, ReadCardCategoryResponse> function = entity -> entityToDto(entity);
        return entityList.stream().map(function).collect(Collectors.toList());
    }

    @Override
    public void updateCardCategory(CreateCardCategoryRequest request) {
        cardCategoryRepository.findById(request.getCardCategoryNo()).ifPresent(cardCategory ->{
            cardCategory.setCardCategoryName(request.getCardCategoryName());
        });
    }
}
