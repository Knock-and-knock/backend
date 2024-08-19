package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateMatchRequest;
import com.shinhan.knockknock.domain.dto.CreateMatchResponse;
import com.shinhan.knockknock.domain.dto.UpdateMatchRequest;
import com.shinhan.knockknock.domain.entity.MatchEntity;

public interface MatchService {
    CreateMatchResponse readMatch();
    CreateMatchResponse createMatch(CreateMatchRequest request);
    CreateMatchResponse updateMatch(UpdateMatchRequest request);
    void deleteMatch(long matchNo);

    default CreateMatchResponse entityToDto(MatchEntity match) {
        return CreateMatchResponse.builder()
                .matchProtectorName(match.getMatchProtectorName())
                .matchProtegeName(match.getMatchProtegeName())
                .protectorUserNo(match.getUserProtectorNo().getUserNo())
                .protegeUserNo(match.getUserProtegeNo().getUserNo())
                .matchStatus(match.getMatchStatus())
                .build();
    }
}
