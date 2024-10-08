package com.shinhan.knockknock.domain.dto.cardhistory;

import com.shinhan.knockknock.domain.entity.CardCategoryEntity;
import com.shinhan.knockknock.domain.entity.CardEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCardHistoryRequest {
    @Schema(example = "55000")
    private int cardHistoryAmount;
    @Schema(example = "올리브영 연남점")
    private String cardHistoryShopname;
    @Schema(example = "2024-08-08 17:30")
    private Timestamp cardHistoryApprove;
    @Schema(example = "1")
    private Long cardCategoryNo;
    @Schema(example = "1")
    private Long cardId;
    @Schema(example = "true")
    private boolean isCardFamily;
    @Schema(example = "false")
    private boolean cardHistoryIsCansle;
}
