package com.shinhan.knockknock.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BioLoginUserRequest {
    private long userNo;
    private String userBioPassword;
}
