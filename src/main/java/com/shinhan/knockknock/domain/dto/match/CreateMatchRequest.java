package com.shinhan.knockknock.domain.dto.match;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMatchRequest {
    @Schema(example = "피보호자02")
    private String protegeName;
    @Schema(example = "01012341234")
    private String protegePhone;
    @Schema(example = "딸")
    private String matchProtectorName;
    @Schema(example = "엄마")
    private String matchProtegeName;
}
