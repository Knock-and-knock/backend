package com.shinhan.knockknock.service.welfarebook;

import com.shinhan.knockknock.domain.dto.welfarebook.CreateWelfareBookRequest;
import com.shinhan.knockknock.domain.dto.welfarebook.ReadWelfareBookResponse;
import com.shinhan.knockknock.domain.entity.WelfareBookEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.WelfareEntity;

import java.util.List;

public interface WelfareBookService {
    // Create
    public Long createWelfareBook(CreateWelfareBookRequest request, Long userNo);

    // Read
    List<ReadWelfareBookResponse> readAllByUserNo(Long userNo);
    ReadWelfareBookResponse readDetail(Long welfareBookNo);
    List<ReadWelfareBookResponse> readAllByLastMonth(Long userNo);

    // Delete
    void deleteWelfareBook(Long welfareBookNo);

    // Entity -> DTO
    default ReadWelfareBookResponse entityToDto(WelfareBookEntity entity) {
        return ReadWelfareBookResponse.builder()
                .welfareBookStartDate(entity.getWelfareBookStartDate())
                .welfareBookEndDate(entity.getWelfareBookEndDate())
                .welfareBookIsCansle(entity.isWelfareBookIsCansle())
                .welfareBookIsComplete(entity.isWelfareBookIsComplete())
                .welfareBookUseTime(entity.getWelfareBookUseTime())
                .userNo(entity.getUser().getUserNo())
                .welfareName(entity.getWelfare().getWelfareName())
                .welfarePrice(entity.getWelfare().getWelfarePrice())
                .build();
    }
}
