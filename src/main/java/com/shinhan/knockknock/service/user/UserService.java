package com.shinhan.knockknock.service.user;

import com.shinhan.knockknock.domain.dto.user.*;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.UserRoleEnum;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

public interface UserService {
    Boolean readUserId(String userId);
    Long readByUserId(String userId);
    Boolean readUserPhone(String phone);
    SingleMessageSentResponse sendSms(String phone, String validationNum);
    CreateUserResponse createUser(CreateUserRequest request);
    ReadUserResponse readUser(long userNo);
    ReadUserResponse updateUser(long userNo, UpdateUserRequest request);
    Boolean deleteUser(long userNo);
    SimplePaymentResponse readSimplePayment(long userNo);
    SimplePaymentResponse createSimplePayment(long userNo, SimplePaymentRequest request);
    SimplePaymentResponse validateSimplePayment(long userNo, SimplePaymentRequest request);

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
        ReadUserResponse response;
        if(user.getUserType().equals(UserRoleEnum.PROTECTOR) &&
                user.getMatchProtector() == null && user.getMatchProtege() == null){
            response = ReadUserResponse.builder()
                    .userNo(user.getUserNo())
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .userType(user.getUserType())
                    .userPhone(user.getUserPhone())
                    .build();
        } else {
            String address = protege.getUserAddress() != null ?
                    protege.getUserAddress().split("/")[0] : null;
            String addressDetail = protege.getUserAddress() != null ?
                    protege.getUserAddress().split("/")[1] : null;
            response = ReadUserResponse.builder()
                    .userNo(user.getUserNo())
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .userType(user.getUserType())
                    .userPhone(user.getUserPhone())
                    .matchNo(protege.getMatchProtege()!=null ?
                            protege.getMatchProtege().getMatchNo(): 0)
                    .protegeName(protege.getUserName())
                    .protegeBirth(protege.getUserBirth())
                    .protegeGender(protege.getUserGender())
                    .protegeHeight(protege.getUserHeight())
                    .protegeWeight(protege.getUserWeight())
                    .protegeDisease(protege.getUserDisease())
                    .protegeAddress(address)
                    .protegeAddressDetail(addressDetail)
                    .build();
        }
        return response;
    }
}
