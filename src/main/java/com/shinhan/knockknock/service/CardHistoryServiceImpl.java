package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateCardHistoryRequest;
import com.shinhan.knockknock.domain.dto.ReadCardHistoryResponse;
import com.shinhan.knockknock.domain.entity.CardHistoryEntity;
import com.shinhan.knockknock.repository.CardHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Function<CardHistoryEntity, ReadCardHistoryResponse> function = entity -> entityToDto(entity);
        return entityList.stream().map(function).collect(Collectors.toList());
    }
}
