package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateWelfareBookRequest;
import com.shinhan.knockknock.domain.dto.ReadWelfareBookResponse;
import com.shinhan.knockknock.domain.entity.WelfareBookEntity;

import java.util.List;

public interface WelfareBookService {
    //Create
    Long createWelfareBook(CreateWelfareBookRequest request);

    //Read
    List<ReadWelfareBookResponse> readAll(Long welfareBookNo);
    ReadWelfareBookResponse readDetail(Long welfareBookNo);

    //Delete
    void deleteWelfareBook(Long welfareBookNo);

    //Dto -> Entity
    default WelfareBookEntity dtoToEntity(CreateWelfareBookRequest request){
        WelfareBookEntity entity = WelfareBookEntity.builder()
                .welfareBookNo(request.getWelfareBookNo())
                .userId(request.getUserId())
                .welfareBookStartDate(request.getWelfareBookStartDate())
                .welfareBookEndDate(request.getWelfareBookEndDate())
                .welfareBookIsCansle(request.isWelfareBookIsCansle())
                .welfareBookIsComplete(request.isWelfareBookIsComplete())
                .build();
        return entity;
    }
    //Entity -> Dto
    default ReadWelfareBookResponse entityToDto(WelfareBookEntity entity){
        ReadWelfareBookResponse response = ReadWelfareBookResponse.builder()
                .welfareBookNo(entity.getWelfareBookNo())
                .userId(entity.getUserId())
                .welfareBookStartDate(entity.getWelfareBookStartDate())
                .welfareBookEndDate(entity.getWelfareBookEndDate())
                .welfareBookIsCansle(entity.isWelfareBookIsCansle())
                .welfareBookIsComplete(entity.isWelfareBookIsComplete())
                .build();
        return response;
    }
}
