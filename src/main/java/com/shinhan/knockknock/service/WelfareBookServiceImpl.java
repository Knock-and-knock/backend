package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateWelfareBookRequest;
import com.shinhan.knockknock.domain.dto.ReadWelfareBookResponse;
import com.shinhan.knockknock.domain.entity.WelfareBookEntity;
import com.shinhan.knockknock.repository.WelfareBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WelfareBookServiceImpl implements WelfareBookService{

    @Autowired
    WelfareBookRepository  welfareBookRepo;

    @Override
    public Long createWelfareBook(CreateWelfareBookRequest request) {
        WelfareBookEntity newWelfareBook = welfareBookRepo.save(dtoToEntity(request));
        return newWelfareBook.getWelfareBookNo();
    }

    @Override
    public List<ReadWelfareBookResponse> readAll() {
        List<WelfareBookEntity> entityList = welfareBookRepo.findAll();
        Function<WelfareBookEntity, ReadWelfareBookResponse> function = entity->entityToDto(entity);
        return entityList.stream().map(function).collect(Collectors.toList());
    }

    @Override
    public ReadWelfareBookResponse readDetail(Long welfareBookNo) {
        Optional<WelfareBookEntity> entity = welfareBookRepo.findById(welfareBookNo);
        return entity.map(this::entityToDto).orElse(null);
    }


    @Override
    public void deleteWelfareBook(Long welfareBookNo) {
        welfareBookRepo.deleteById(welfareBookNo);
    }
}
