package com.shinhan.knockknock.service.match;

import com.shinhan.knockknock.domain.dto.match.CreateMatchRequest;
import com.shinhan.knockknock.domain.dto.match.CreateMatchResponse;
import com.shinhan.knockknock.domain.dto.match.UpdateMatchRequest;
import com.shinhan.knockknock.domain.entity.MatchEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.UserRoleEnum;
import com.shinhan.knockknock.repository.MatchRepository;
import com.shinhan.knockknock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    @Override
    public CreateMatchResponse readMatch() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));
        MatchEntity match = matchRepository.findByUserProtectorOrUserProtege(user, user)
                .orElseThrow(() -> new NoSuchElementException("매칭이 존재하지 않습니다."));
        return entityToDto(match);
    }

    @Override
    public CreateMatchResponse createMatch(CreateMatchRequest request) {
        // 로그인된 사용자 인증 정보로 보호자 UserEntity 객체 가져오기
        String protectorId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity protectorUser = userRepository.findByUserId(protectorId)
                .orElseThrow(() -> new UsernameNotFoundException("아이디가 존재하지 않습니다."));
        if(!protectorUser.getUserType().equals(UserRoleEnum.PROTECTOR)) {
            throw new RuntimeException("피보호자는 매칭 요청이 불가능합니다.");
        }

        // 피보호자 계정 존재 여부 확인
        String userPhone = request.getProtegePhone().replace("-", "");
        UserEntity protegeUser = userRepository.findByUserNameAndUserPhone(
                        request.getProtegeName(), userPhone)
                .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));
        // 기존 매칭 기록 확인
        MatchEntity match = matchRepository.findByUserProtectorAndUserProtege(protectorUser, protegeUser)
                .orElse(MatchEntity.builder()
                        .matchProtectorName(request.getMatchProtectorName())
                        .matchProtegeName(request.getMatchProtegeName())
                        .userProtector(protectorUser)
                        .userProtege(protegeUser)
                        .matchStatus("WAIT")
                        .build());

        if (match.getMatchStatus().equals("ACCEPT")) {
            throw new RuntimeException("이미 매칭된 회원입니다.");
        } else if (match.getMatchStatus().equals("REJECT") |
        match.getMatchStatus().equals("WAIT")) {
            match.setMatchStatus("WAIT");
            match.setMatchProtectorName(request.getMatchProtectorName());
            match.setMatchProtegeName(request.getMatchProtegeName());
        }

        MatchEntity newMatch = matchRepository.save(match);

        return entityToDto(newMatch);
    }

    @Override
    public CreateMatchResponse updateMatch(UpdateMatchRequest request) {
        // 로그인된 사용자 인증 정보로 UserEntity 객체 가져와서 피보호자인지 검증
        String protegeId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity protegeUser = userRepository.findByUserId(protegeId)
                .orElseThrow(() -> new UsernameNotFoundException("아이디가 존재하지 않습니다."));

        if (protegeUser.getUserType().toString().equals("PROTECTOR")) {
            throw new RuntimeException("잘못된 접근입니다.");
        }

        MatchEntity match = matchRepository.findById(request.getMatchNo())
                .orElseThrow(() -> new NoSuchElementException("매칭이 존재하지 않습니다."));

        switch (match.getMatchStatus()) {
            case "WAIT" -> {
                if (request.getMatchStatus().equals("ACCEPT") | request.getMatchStatus().equals("REJECT")) {
                    match.setMatchStatus(request.getMatchStatus());
                } else {
                    throw new RuntimeException("잘못된 응답입니다.");
                }
            }
            case "ACCEPT" -> throw new RuntimeException("이미 수락한 요청입니다.");
            case "REJECT" -> throw new RuntimeException("이미 거절한 요청입니다.");
            default -> throw new RuntimeException("잘못된 응답입니다.");
        }

        MatchEntity updateMatch = matchRepository.save(match);

        return entityToDto(updateMatch);
    }

    @Override
    public void deleteMatch(long matchNo) {
        // 로그인한 user entity 조회
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));

        MatchEntity match = matchRepository.findById(matchNo)
                .orElseThrow(() -> new NoSuchElementException("매칭이 존재하지 않습니다."));

        if (user.getUserType().toString().equals("PROTEGE")
                && Objects.equals(user.getUserNo(), match.getUserProtege().getUserNo())
                && match.getMatchStatus().equals("ACCEPT")) {
            matchRepository.delete(match);
        } else {
            throw new RuntimeException("잘못된 접근입니다.");
        }
    }
}
