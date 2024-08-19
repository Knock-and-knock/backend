package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateCardHistoryRequest;
import com.shinhan.knockknock.domain.dto.ReadCardHistoryResponse;
import com.shinhan.knockknock.domain.entity.CardHistoryEntity;
import com.shinhan.knockknock.repository.CardHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CardHistoryServiceImpl implements CardHistoryService{

    @Autowired
    CardHistoryRepository cardHistoryRepo;
    @Override
    public Long createCardHistory(CreateCardHistoryRequest request) {
        CardHistoryEntity newCardHistory = cardHistoryRepo.save(dtoToEntity(request));
        return newCardHistory.getCardHistoryNo();
    }

    @Override
    public List<ReadCardHistoryResponse> readAll() {
        List<CardHistoryEntity> entityList = cardHistoryRepo.findAll();
        if (entityList.isEmpty()) {
            // 조회 결과가 없을 때 예외를 던짐
            throw new NoSuchElementException("카드 내역이 존재하지 않습니다.");
        }
        Function<CardHistoryEntity, ReadCardHistoryResponse> function = this::entityToDto;
        return entityList.stream().map(function).collect(Collectors.toList());
    }
}
