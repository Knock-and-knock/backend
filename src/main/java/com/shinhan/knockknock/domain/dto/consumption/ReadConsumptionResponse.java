package com.shinhan.knockknock.domain.dto.consumption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadConsumptionResponse {
    private Long cardId;
    private String categoryName;
    private boolean isFamily;
    private int totalAmount;
    private int amount;
}
