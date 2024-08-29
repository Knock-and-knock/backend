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
public class UpdateMatchRequest {
    @Schema(example = "6")
    private long matchNo;
    @Schema(example = "ACCEPT")
    private String matchStatus;
}
