package com.shinhan.knockknock.domain.dto;

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
    private Long cardHistoryNo;
    private int cardHistoryAmount;
    private String cardHistoryShopname;
    private Timestamp cardHistoryApprove;
    private Long cardCategoryNo;
    private String cardId;
}
