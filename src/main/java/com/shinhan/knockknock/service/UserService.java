package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateUserRequest;
import com.shinhan.knockknock.domain.dto.ReadUserResponse;
import com.shinhan.knockknock.domain.dto.UpdateUserRequest;
import com.shinhan.knockknock.domain.entity.UserEntity;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

public interface UserService {
    Boolean readUserId(String userId);
    Boolean readUserPhone(String phone);
    SingleMessageSentResponse sendSms(String phone, String validationNum);
    Boolean createUser(CreateUserRequest request);
    ReadUserResponse readUser(long userNo);
    ReadUserResponse updateUser(long userNo, UpdateUserRequest request);

    default UserEntity dtoToEntity(CreateUserRequest request){
        return UserEntity.builder()
                .userId(request.getUserId())
                .userPassword(request.getUserPassword())
                .userName(request.getUserName())
                .userPhone(request.getUserPhone())
                .userType(request.getUserType())
                .userSimplePassword(request.getUserSimplePassword())
                .build();
    }

    default ReadUserResponse entityToDtoProtector(UserEntity user, UserEntity protege){
        return ReadUserResponse.builder()
                .userNo(user.getUserNo())
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userType(user.getUserType())
                .userPhone(user.getUserPhone())
                .protegeName(protege.getUserName())
                .protegeBirth(protege.getUserBirth())
                .protegeGender(protege.getUserGender())
                .protegeHeight(protege.getUserHeight())
                .protegeWeight(protege.getUserWeight())
                .protegeDisease(protege.getUserDisease())
                .protegeAddress(protege.getUserAddress())
                .build();
    }
}
