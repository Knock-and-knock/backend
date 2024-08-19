package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateWelfareBookRequest;
import com.shinhan.knockknock.domain.dto.ReadWelfareBookResponse;
import com.shinhan.knockknock.domain.entity.WelfareBookEntity;

import java.util.List;

public interface WelfareBookService {
    //Create
    Long createWelfareBook(CreateWelfareBookRequest request);

    //Read
    List<ReadWelfareBookResponse> readAll();
    ReadWelfareBookResponse readDetail(Long welfareBookNo);

    //Delete
    void deleteWelfareBook(Long welfareBookNo);

    //Dto -> Entity
    default WelfareBookEntity dtoToEntity(CreateWelfareBookRequest request){
        WelfareBookEntity entity = WelfareBookEntity.builder()
                .welfareBookNo(request.getWelfareBookNo())
                .welfareBookStartDate(request.getWelfareBookStartDate())
                .welfareBookEndDate(request.getWelfareBookEndDate())
                .welfareBookIsCansle(request.isWelfareBookIsCansle())
                .welfareBookIsComplete(request.isWelfareBookIsComplete())
                .userNo(request.getUserNo())
                .welfareNo(request.getWelfareNo())
                .build();
        return entity;
    }
    //Entity -> Dto
    default ReadWelfareBookResponse entityToDto(WelfareBookEntity entity){
        ReadWelfareBookResponse response = ReadWelfareBookResponse.builder()
                .welfareBookStartDate(entity.getWelfareBookStartDate())
                .welfareBookEndDate(entity.getWelfareBookEndDate())
                .welfareBookIsCansle(entity.isWelfareBookIsCansle())
                .welfareBookIsComplete(entity.isWelfareBookIsComplete())
                .userNo(entity.getUserNo())
                .welfareNo(entity.getWelfareNo())
                .build();
        return response;
    }
}
