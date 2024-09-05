package com.shinhan.knockknock.service.user;

import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.user.BioLoginUserRequest;
import com.shinhan.knockknock.domain.dto.user.IdLoginUserRequest;
import com.shinhan.knockknock.domain.dto.user.SimpleLoginUserRequest;
import com.shinhan.knockknock.domain.dto.user.TokenResponse;
import com.shinhan.knockknock.domain.entity.TokenEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.repository.TokenRepository;
import com.shinhan.knockknock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public TokenResponse loginUser(IdLoginUserRequest request) {
        String userId = request.getUserId();
        String userPassword = request.getUserPassword();
        UserEntity user = userRepository.findByUserId(userId).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
        }

        if (!passwordEncoder.matches(userPassword, user.getUserPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return issueToken(user);
    }

    public TokenResponse simpleLoginUser(SimpleLoginUserRequest request) {
        String simplePassword = request.getUserSimplePassword();

        UserEntity user = userRepository.findById(request.getUserNo())
                .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));

        if (!passwordEncoder.matches(simplePassword, user.getUserSimplePassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return issueToken(user);
    }

    public TokenResponse bioLoginUser(BioLoginUserRequest request) {
        String bioPassword = request.getUserBioPassword();

        UserEntity user = userRepository.findById(request.getUserNo())
                .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));
        if (!passwordEncoder.matches(bioPassword, user.getUserBioPassword())) {
            throw new BadCredentialsException("생체 데이터가 일치하지 않습니다.");
        }
        return issueToken(user);
    }

    public void logoutUser(long userNo) {
        TokenEntity tokenEntity = tokenRepository.findByUser_UserNo(userNo)
                .orElse(null);
        try {
            if (tokenEntity != null) {
                tokenRepository.delete(tokenEntity);
            }
        } catch (Exception e) {
            throw new RuntimeException("로그아웃에 실패하였습니다.");
        }
    }

    private TokenResponse issueToken(UserEntity user) {
        // access, refresh 토큰 발급
        String accessToken = jwtProvider.createAccessToken(user.entityToDto());
        String refreshToken = jwtProvider.createRefreshToken(user.entityToDto());

        // db에 refresh 토큰 저장
        TokenEntity tokenEntity = tokenRepository.findByUser(user)
                .orElse(TokenEntity.builder()
                        .user(user)
                        .build());
        tokenEntity.setRefreshToken(refreshToken);
        tokenRepository.save(tokenEntity);

        return TokenResponse.builder()
                .userNo(user.getUserNo())
                .userType(user.getUserType())
                .userBioPassword(user.getUserBioPassword())
                .accessToken(accessToken)
                .build();
    }
}
