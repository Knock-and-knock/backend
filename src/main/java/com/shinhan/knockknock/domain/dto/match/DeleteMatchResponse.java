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
public class DeleteMatchResponse {
    @Schema(example = "매칭이 중단되었습니다.")
    private String message;
}
