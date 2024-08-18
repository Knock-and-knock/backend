package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateMatchRequest;
import com.shinhan.knockknock.domain.dto.CreateMatchResponse;
import com.shinhan.knockknock.domain.entity.MatchEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;

public interface MatchService {
    CreateMatchResponse createMatch(CreateMatchRequest request);

    default MatchEntity dtoToEntity(CreateMatchRequest request) {
        UserEntity protector = UserEntity.builder()
                .userNo(request.getMatchProtectorNo())
                .build();
        UserEntity protege = UserEntity.builder()
                .userNo(request.getMatchProtegeNo())
                .build();
        return MatchEntity.builder()
                .matchProtectorName(request.getMatchProtectorName())
                .matchProtegeName(request.getMatchProtegeName())
                .userProtectorNo(protector)
                .userProtegeNo(protege)
                .build();
    }
}
