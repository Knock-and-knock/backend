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
public class CreateUserRequest {
    @Schema(example = "protector03")
    private String userId;
    @Schema(example = "1234")
    private String userPassword;
    @Schema(example = "보호자03")
    private String userName;
    @Schema(example = "01099998888")
    private String userPhone;
    @Schema(example = "PROTECTOR")
    private UserRoleEnum userType;
    @Schema(example = "123456")
    private String userSimplePassword;
    private Boolean isBioLogin;
}
