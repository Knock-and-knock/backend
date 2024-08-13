package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateWelfareRequest;
import com.shinhan.knockknock.domain.dto.ReadWelfareResponse;
import com.shinhan.knockknock.domain.entity.WelfareEntity;
import com.shinhan.knockknock.repository.WelfareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WelfareServiceImpl implements WelfareService{

    @Autowired
    WelfareRepository welfareRepo;

    @Override
    public Long createWelfare(CreateWelfareRequest request) {
        WelfareEntity newWelfare = welfareRepo.save(dtoToEntity(request));
        return newWelfare.getWelfareNo();
    }

    @Override
    public List<ReadWelfareResponse> readAll(Long welfareNo) {
        List<WelfareEntity> entityList = welfareRepo.findAllById(Collections.singleton(welfareNo));
        Function<WelfareEntity, ReadWelfareResponse> fn = en->entityToDto(en);
        return entityList.stream().map(fn).collect(Collectors.toList());
    }

    @Override
    public void updateWelfare(CreateWelfareRequest request) {
        welfareRepo.findById(request.getWelfareNo()).ifPresent(welfare ->{
            welfare.setWelfareName(request.getWelfareName());
            welfare.setWelfarePrice(request.getWelfarePirce());
            welfare.setWelfareCategory(request.getWelfareCategory());
        });
    }

    @Override
    public void deleteWelfare(Long welfareNo) {
        welfareRepo.deleteById(welfareNo);
    }
}