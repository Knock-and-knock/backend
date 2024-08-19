package com.shinhan.knockknock.domain.dto;

import com.shinhan.knockknock.domain.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserResponse {
    private Long userNo;
    private String userId;
    private String userPassword;
    private String userName;
    private UserRole userType;
    private String userSimplePassword;
    private String message;
}