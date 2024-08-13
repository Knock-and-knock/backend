package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.WelfareRequest;
import com.shinhan.knockknock.domain.dto.WelfareResponse;
import com.shinhan.knockknock.domain.entity.WelfareEntity;

import java.util.List;

public interface WelfareService {
    //Create
    Long createWelfare(WelfareRequest request);

    //Read
    List<WelfareResponse> readAll(Long welfareNo);

    //Update
    void updateWelfare(WelfareRequest request);

    //Delete
    void deleteWelfare(Long welfareNo);

    //Dto -> Entity
    default WelfareEntity dtoToEntity(WelfareRequest request){
        WelfareEntity entity = WelfareEntity.builder()
                .welfareNo(request.getWelfareNo())
                .welfareName(request.getWelfareName())
                .welfarePrice(request.getWelfarePirce())
                .welfareCategory(request.getWelfareCategory())
                .build();
        return entity;
    }

    //Entity -> Dto
    default WelfareResponse entityToDto(WelfareEntity entity){
        WelfareResponse response = WelfareResponse.builder()
                .welfareNo(entity.getWelfareNo())
                .welfareName(entity.getWelfareName())
                .welfarePirce(entity.getWelfarePrice())
                .welfareCategory(entity.getWelfareCategory())
                .build();
        return response;
    }
}
