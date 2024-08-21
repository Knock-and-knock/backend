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
public class CreateMatchResponse {
    @Schema(example = "보호자02")
    private String matchProtectorName;
    @Schema(example = "피보호자02")
    private String matchProtegeName;
    @Schema(example = "38")
    private long protectorUserNo;
    @Schema(example = "3")
    private long protegeUserNo;
    @Schema(example = "WAIT")
    private String matchStatus;
    @Schema(example = "매칭 요청 성공")
    private String message;
}
