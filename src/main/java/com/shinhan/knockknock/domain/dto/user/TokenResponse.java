package com.shinhan.knockknock.domain.dto.user;

import com.shinhan.knockknock.domain.entity.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    @Schema(example = "1")
    private long userNo;
    @Schema(example = "PROTECTOR")
    private UserRoleEnum userType;
    @Schema(example = "accessToken")
    private String accessToken;
    private String userBioPassword;
    private String message;
}
