package com.shinhan.knockknock.service.welfarebook;

import com.shinhan.knockknock.domain.dto.welfarebook.CreateWelfareBookRequest;
import com.shinhan.knockknock.domain.dto.welfarebook.ReadWelfareBookResponse;
import com.shinhan.knockknock.domain.entity.WelfareBookEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.WelfareEntity;
import org.springframework.data.domain.Sort;

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
                .welfareBookNo(entity.getWelfareBookNo())
                .welfareBookStartDate(entity.getWelfareBookStartDate())
                .welfareBookEndDate(entity.getWelfareBookEndDate())
                .welfareBookIsCansle(entity.isWelfareBookIsCansle())
                .welfareBookIsComplete(entity.isWelfareBookIsComplete())
                .welfareTotalPrice(entity.getWelfareTotalPrice())
                .welfareBookReservationDate(entity.getWelfareBookReservationDate())
                .welfareBookUseTime(entity.getWelfareBookUseTime())
                .userName(entity.getUser().getUserName())
                .userGender(entity.getUserGender())
                .welfareName(entity.getWelfareName())
                .welfareCategory(entity.getWelfare().getWelfareCategory())
                .build();
    }
}
