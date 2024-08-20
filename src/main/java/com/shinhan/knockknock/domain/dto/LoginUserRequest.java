package com.shinhan.knockknock.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserRequest {
    @Schema(example = "protector01")
    private String userId;
    @Schema(example = "1234")
    private String userPassword;
    @Schema(example = "NORMAL")
    private String loginType;
    @Schema(example = "string")
    private String userSimplePassword;
}
