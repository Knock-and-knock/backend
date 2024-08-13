package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.WelfareBookRequest;
import com.shinhan.knockknock.domain.dto.WelfareBookResponse;
import com.shinhan.knockknock.domain.entity.WelfareBookEntity;

import java.util.List;

public interface WelfareBookService {
    //Create
    Long createWelfareBook(WelfareBookRequest request);

    //Read
    List<WelfareBookResponse> readAll(Long welfareBookNo);
    WelfareBookResponse readDetail(Long welfareBookNo);

    //Delete
    void deleteWelfareBook(Long welfareBookNo);

    //Dto -> Entity
    default WelfareBookEntity dtoToEntity(WelfareBookRequest request){
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
    default WelfareBookResponse entityToDto(WelfareBookEntity entity){
        WelfareBookResponse response = WelfareBookResponse.builder()
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
