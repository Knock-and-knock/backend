package com.shinhan.knockknock.service;

import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.LoginUserRequest;
import com.shinhan.knockknock.domain.dto.TokenResponse;
import com.shinhan.knockknock.domain.entity.UserEntity;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public TokenResponse loginUser(LoginUserRequest request) {
        String userId = request.getUserId();
        String userPassword = request.getUserPassword();
        UserEntity user = userRepository.findByUserId(userId).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
        }

        if (!passwordEncoder.matches(userPassword, user.getUserPassword())){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtProvider.createAccessToken(user.entityToDto());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
