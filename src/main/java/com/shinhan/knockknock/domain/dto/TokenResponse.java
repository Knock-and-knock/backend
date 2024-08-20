package com.shinhan.knockknock.domain.dto;

import com.shinhan.knockknock.domain.entity.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private long userNo;
    private UserRoleEnum userType;
    private String accessToken;
    private String message;
}
