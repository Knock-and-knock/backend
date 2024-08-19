package com.shinhan.knockknock.domain.dto;

import com.shinhan.knockknock.domain.entity.LoginTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserRequest {
    private String userId;
    private String userPassword;
    private LoginTypeEnum loginType;
}
