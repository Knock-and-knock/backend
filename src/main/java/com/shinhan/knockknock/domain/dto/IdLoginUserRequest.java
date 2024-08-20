package com.shinhan.knockknock.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class IdLoginUserRequest {
    private String userId;
    private String userPassword;
}
