package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateWelfareBookRequest;
import com.shinhan.knockknock.domain.dto.ReadWelfareBookResponse;
import com.shinhan.knockknock.domain.entity.WelfareBookEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.WelfareEntity;

import java.util.List;

public interface WelfareBookService {
    // Create
    Long createWelfareBook(CreateWelfareBookRequest request);

    // Read
    List<ReadWelfareBookResponse> readAllByUserNo(Long userNo);
    ReadWelfareBookResponse readDetail(Long welfareBookNo);

    // Delete
    void deleteWelfareBook(Long welfareBookNo);

    // DTO -> Entity
    default WelfareBookEntity dtoToEntity(CreateWelfareBookRequest request, UserEntity user, WelfareEntity welfare) {
        return WelfareBookEntity.builder()
                .welfareBookNo(request.getWelfareBookNo())
                .welfareBookStartDate(request.getWelfareBookStartDate())
                .welfareBookEndDate(request.getWelfareBookEndDate())
                .welfareBookIsCansle(request.isWelfareBookIsCansle())
                .welfareBookIsComplete(request.isWelfareBookIsComplete())
                .user(user)
                .welfare(welfare)
                .build();
    }

    // Entity -> DTO
    default ReadWelfareBookResponse entityToDto(WelfareBookEntity entity) {
        return ReadWelfareBookResponse.builder()
                .welfareBookStartDate(entity.getWelfareBookStartDate())
                .welfareBookEndDate(entity.getWelfareBookEndDate())
                .welfareBookIsCansle(entity.isWelfareBookIsCansle())
                .welfareBookIsComplete(entity.isWelfareBookIsComplete())
                .userNo(entity.getUserNo())
                .welfareName(entity.getWelfareName())
                .welfarePirce(entity.getWelfarePrice())
                .build();
    }
}