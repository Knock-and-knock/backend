package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.LoginUserRequest;
import com.shinhan.knockknock.domain.dto.LoginUserResponse;
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

    public LoginUserResponse loginUser(LoginUserRequest request) {
        String userId = request.getUserId();
        String userPassword = request.getUserPassword();
        UserEntity user = userRepository.findByUserId(userId).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
        }

        if (!passwordEncoder.matches(userPassword, user.getUserPassword())){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return LoginUserResponse.builder()
                .userNo(user.getUserNo())
                .userName(user.getUserName())
                .role(user.getUserType())
                .build();
    }
}
