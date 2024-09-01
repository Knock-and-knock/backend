package com.shinhan.knockknock.domain.dto.cardhistory;

import com.shinhan.knockknock.domain.entity.CardCategoryEntity;
import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadCardHistoryResponse {
    @Schema(example = "1")
    private Long cardHistoryNo;
    @Schema(example = "55000")
    private int cardHistoryAmount;
    @Schema(example = "올리브영 연남점")
    private String cardHistoryShopname;
    @Schema(example = "2024-08-08 17:30")
    private Timestamp cardHistoryApprove;
    @Schema(example = "True")
    private boolean isCardFamily;
    @Schema(example = "false")
    private boolean cardHistoryIsCansle;
}
