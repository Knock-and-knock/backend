package com.shinhan.knockknock.domain.dto.conversation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionReportResponse {
    private int year;
    private int month;

    private int promptTokens;
    private int completionTokens;
    private int totalTokens;
}
