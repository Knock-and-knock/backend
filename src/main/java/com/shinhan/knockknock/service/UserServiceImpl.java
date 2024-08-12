package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public Boolean readUserId(String userId) {
        UserEntity findUser = userRepository.findByUserId(userId);
        return findUser != null;
    }
}
