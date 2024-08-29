package com.shinhan.knockknock.service.match;

import com.shinhan.knockknock.domain.dto.match.CreateMatchRequest;
import com.shinhan.knockknock.domain.dto.match.CreateMatchResponse;
import com.shinhan.knockknock.domain.dto.match.UpdateMatchRequest;
import com.shinhan.knockknock.domain.entity.MatchEntity;

public interface MatchService {
    CreateMatchResponse readMatch();
    CreateMatchResponse createMatch(CreateMatchRequest request);
    CreateMatchResponse updateMatch(UpdateMatchRequest request);
    void deleteMatch(long matchNo);

    default CreateMatchResponse entityToDto(MatchEntity match) {
        return CreateMatchResponse.builder()
                .matchNo(match.getMatchNo())
                .matchProtectorName(match.getMatchProtectorName())
                .protectorUserName(match.getUserProtector().getUserName())
                .matchProtegeName(match.getMatchProtegeName())
                .protegeUserName(match.getUserProtege().getUserName())
                .protectorUserNo(match.getUserProtector().getUserNo())
                .protegeUserNo(match.getUserProtege().getUserNo())
                .matchStatus(match.getMatchStatus())
                .build();
    }
}
