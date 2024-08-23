package com.shinhan.knockknock.domain.dto.conversationroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private boolean actionRequired;
    private int serviceTypeNumber;
    private String reservationDate;
    private int reservationTimeNumber;
}
