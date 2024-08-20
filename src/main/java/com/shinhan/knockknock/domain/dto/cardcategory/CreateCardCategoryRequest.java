package com.shinhan.knockknock.domain.dto.cardcategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCardCategoryRequest {
    @Schema(example = "1")
    private Long cardCategoryNo;
    @Schema(example = "편의점·마트·잡화")
    private String cardCategoryName;
}
