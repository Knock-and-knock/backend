package com.shinhan.knockknock.service.welfare;

import com.shinhan.knockknock.domain.dto.welfare.CreateWelfareRequest;
import com.shinhan.knockknock.domain.dto.welfare.ReadWelfareResponse;
import com.shinhan.knockknock.domain.entity.WelfareEntity;

import java.util.List;

public interface WelfareService {
    //Create
    Long createWelfare(CreateWelfareRequest request);

    //Read
    List<ReadWelfareResponse> readAll();

    //Update
    void updateWelfare(CreateWelfareRequest request);

    //Delete
    void deleteWelfare(Long welfareNo);

    //Dto -> Entity
    default WelfareEntity dtoToEntity(CreateWelfareRequest request){
        WelfareEntity entity = WelfareEntity.builder()
                .welfareNo(request.getWelfareNo())
                .welfareName(request.getWelfareName())
                .welfarePrice(request.getWelfarePirce())
                .welfareCategory(request.getWelfareCategory())
                .build();
        return entity;
    }

    //Entity -> Dto
    default ReadWelfareResponse entityToDto(WelfareEntity entity){
        ReadWelfareResponse response = ReadWelfareResponse.builder()
                .welfareNo(entity.getWelfareNo())
                .welfareName(entity.getWelfareName())
                .welfarePirce(entity.getWelfarePrice())
                .welfareCategory(entity.getWelfareCategory())
                .build();
        return response;
    }
}
