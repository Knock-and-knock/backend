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
public class SimpleLoginUserRequest {
    private long userNo;
    private String userSimplePassword;
    private LoginTypeEnum loginType;
}
