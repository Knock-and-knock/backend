package com.shinhan.knockknock.domain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleLoginUserRequest{
    private long userNo;
    private String userSimplePassword;

}
