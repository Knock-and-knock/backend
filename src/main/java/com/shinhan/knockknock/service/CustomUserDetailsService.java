package com.shinhan.knockknock.service;

import com.shinhan.knockknock.auth.CustomUserDetails;
import com.shinhan.knockknock.domain.dto.LoginUserResponse;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userNo) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findById(Long.parseLong(userNo))
                .orElseThrow(() -> new UsernameNotFoundException(userNo + " 사용자를 찾을 수 없습니다."));
        return new CustomUserDetails(entityToDto(userEntity));
    }

    public LoginUserResponse entityToDto(UserEntity userEntity) {
        return LoginUserResponse.builder()
                .userNo(userEntity.getUserNo())
                .userId(userEntity.getUserId())
                .userPassword(userEntity.getUserPassword())
                .userName(userEntity.getUserName())
                .userType(userEntity.getUserType())
                .userSimplePassword(userEntity.getUserSimplePassword())
                .build();
    }
}
