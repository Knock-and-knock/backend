package com.shinhan.knockknock.service.cardcategory;

import com.shinhan.knockknock.domain.dto.cardcategory.CreateCardCategoryRequest;
import com.shinhan.knockknock.domain.dto.cardcategory.ReadCardCategoryResponse;
import com.shinhan.knockknock.domain.entity.CardCategoryEntity;
import com.shinhan.knockknock.repository.CardCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CardCategoryServiceImpl implements CardCategoryService {

    @Autowired
    CardCategoryRepository cardCategoryRepository;

    @Override
    public Long createCardCategory(CreateCardCategoryRequest request) {
        try {
            CardCategoryEntity newCardCategory = cardCategoryRepository.save(dtoToEntity(request));
            return newCardCategory.getCardCategoryNo();
        } catch (DataAccessException e) {
            throw new RuntimeException("카드 카테고리 생성 중 데이터베이스 오류가 발생했습니다.", e);
        } catch (Exception e) {
            throw new RuntimeException("카드 카테고리 생성에 실패했습니다.", e);
        }
    }

    @Override
    public List<ReadCardCategoryResponse> readAll() {
        try {
            List<CardCategoryEntity> entityList = cardCategoryRepository.findAll();
            if (entityList.isEmpty()) {
                throw new NoSuchElementException("카드 카테고리가 존재하지 않습니다.");
            }
            Function<CardCategoryEntity, ReadCardCategoryResponse> function = entity -> entityToDto(entity);
            return entityList.stream().map(function).collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            throw e;  // 컨트롤러에서 처리
        } catch (Exception e) {
            throw new RuntimeException("카드 카테고리 조회에 실패했습니다.", e);
        }
    }

    @Override
    public void updateCardCategory(CreateCardCategoryRequest request) {
        try {
            CardCategoryEntity cardCategory = cardCategoryRepository.findById(request.getCardCategoryNo())
                    .orElseThrow(() -> new NoSuchElementException("해당 카드 카테고리가 존재하지 않습니다."));
            cardCategory.setCardCategoryName(request.getCardCategoryName());
            cardCategoryRepository.save(cardCategory);
        } catch (NoSuchElementException e) {
            throw e;  // 컨트롤러에서 처리
        } catch (Exception e) {
            throw new RuntimeException("카드 카테고리 수정에 실패했습니다.", e);
        }
    }
}
