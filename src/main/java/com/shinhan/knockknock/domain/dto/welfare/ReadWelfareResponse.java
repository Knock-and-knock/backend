package com.shinhan.knockknock.domain.dto.welfare;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadWelfareResponse {
    @Schema(example = "1")
    private Long welfareNo;
    @Schema(example = "복지 서비스 이름")
    private String welfareName;
    @Schema(example = "6000")
    private long welfarePirce;
    @Schema(example = "돌봄")
    private String welfareCategory;
}
