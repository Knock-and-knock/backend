package com.shinhan.knockknock.domain.dto.conversation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationResponse {
    private String mainTaskNumber;
    private String subTaskNumber;

    private int promptTokens;
    private int completionTokens;
    private int totalTokens;
}
