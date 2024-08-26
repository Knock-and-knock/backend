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
public class IdLoginUserRequest {
    @Schema(example = "protege01")
    private String userId;
    @Schema(example = "1234")
    private String userPassword;
}
