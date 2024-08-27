package com.shinhan.knockknock.domain.dto.conversation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedirectionResponse {
    private boolean actionRequired;
    private String serviceNumber;
    private String serviceName;
    private String serviceUrl;

    private int promptTokens;
    private int completionTokens;
    private int totalTokens;
}
