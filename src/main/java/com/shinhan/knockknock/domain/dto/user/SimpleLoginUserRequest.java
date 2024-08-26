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
public class SimpleLoginUserRequest{
    @Schema(example = "38")
    private long userNo;
    @Schema(example = "123456")
    private String userSimplePassword;

}
