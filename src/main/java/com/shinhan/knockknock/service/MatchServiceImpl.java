package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateMatchRequest;
import com.shinhan.knockknock.domain.dto.CreateMatchResponse;
import com.shinhan.knockknock.domain.entity.MatchEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.repository.MatchRepository;
import com.shinhan.knockknock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    @Override
    public CreateMatchResponse createMatch(CreateMatchRequest request) {
        // 로그인된 사용자 인증 정보로 보호자 UserEntity 객체 가져오기
        String protectorId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity protectorUser = userRepository.findByUserId(protectorId)
                .orElseThrow(() -> new UsernameNotFoundException("아이디가 존재하지 않습니다."));

        // 피보호자 계정 존재 여부 확인
        UserEntity protegeUser = userRepository.findByUserNameAndUserPhone(
                        request.getProtegeName(), request.getProtegePhone())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // 기존 매칭 기록 확인
        MatchEntity match = matchRepository.findByUserProtectorNoAndUserProtegeNo(protectorUser, protegeUser)
                .orElse(MatchEntity.builder()
                        .matchProtectorName(request.getMatchProtectorName())
                        .matchProtegeName(request.getMatchProtegeName())
                        .userProtectorNo(protectorUser)
                        .userProtegeNo(protegeUser)
                        .build());
        if(match.getMatchStatus().equals("ACCEPT")) {
            throw new RuntimeException("이미 매칭된 회원입니다.");
        }

        matchRepository.save(match);

        return CreateMatchResponse.builder()
                .matchProtectorName(match.getMatchProtectorName())
                .matchProtegeName(match.getMatchProtegeName())
                .protectorUserNo(match.getUserProtectorNo().getUserNo())
                .protegeUserNo(match.getUserProtegeNo().getUserNo())
                .build();
    }
}
