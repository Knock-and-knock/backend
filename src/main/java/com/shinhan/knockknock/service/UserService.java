package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateUserRequest;
import com.shinhan.knockknock.domain.entity.UserEntity;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

public interface UserService {
    Boolean readUserId(String userId);
    Boolean readUserPhone(String phone);
    SingleMessageSentResponse sendSms(String phone, String validationNum);
    Boolean createUser(CreateUserRequest request);

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
}
