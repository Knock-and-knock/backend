package com.shinhan.knockknock.domain.dto.user;

import com.shinhan.knockknock.domain.entity.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadUserResponse {
    private long userNo;
    private String userId;
    private String userName;
    private String userPhone;
    private UserRoleEnum userType;
    private long matchNo;
    private String protegeName;
    private Date protegeBirth;
    private int protegeGender;
    private int protegeHeight;
    private int protegeWeight;
    private String protegeDisease;
    private String protegeAddress;
    private String protegeAddressDetail;
    private String message;
}
