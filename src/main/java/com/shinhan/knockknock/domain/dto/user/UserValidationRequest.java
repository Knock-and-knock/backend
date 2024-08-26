package com.shinhan.knockknock.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserValidationRequest {
    @Schema(example = "01012345678")
    private String phone;
    @Schema(example = "123456")
    private String validationNum;
}
