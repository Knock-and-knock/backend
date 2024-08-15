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
    private String userName;
    private UserRole role;
    private String message;
}
