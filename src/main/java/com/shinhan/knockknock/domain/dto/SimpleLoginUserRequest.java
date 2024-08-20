package com.shinhan.knockknock.domain.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SimpleLoginUserRequest extends LoginUserRequest{
    private long userNo;
    private String userSimplePassword;

}
