package com.shinhan.knockknock.domain.dto.conversation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {

    private String content;
    private String audioData;
    private boolean actionRequired;
    private int totalTokens;

    private RedirectionResult redirectionResult;
    private ReservationResult reservationResult;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RedirectionResult {
        private String serviceNumber;
        private String serviceName;
        private String serviceUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationResult {
        private int welfareNo;
        private String welfareBookStartDate;
        private String welfareBookEndDate;
        private int welfareBookUseTime;
    }
}
