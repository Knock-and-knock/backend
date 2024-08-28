package com.shinhan.knockknock.domain.dto.conversation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private boolean actionRequired;
    private int serviceTypeNumber;
    private String reservationDate;
    private int reservationTimeNumber;

    private int promptTokens;
    private int completionTokens;
    private int totalTokens;
}
