package com.shinhan.knockknock.domain.dto.conversation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotResponse {
    private String content;
    private int promptTokens;
    private int completionTokens;
    private int totalTokens;
    private boolean actionRequired;

    private RedirectionResponse redirectionResult;
    private ReservationResponse reservationResult;
}
