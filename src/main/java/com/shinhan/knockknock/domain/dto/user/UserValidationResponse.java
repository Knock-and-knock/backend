package com.shinhan.knockknock.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserValidationResponse {
    @Schema(example = "사용가능한 아이디입니다.")
    private String message;
    @Schema(example = "true")
    private Boolean result;
}
