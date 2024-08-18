package com.shinhan.knockknock.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMatchResponse {
    private String matchProtectorName;
    private String matchProtegeName;
    private long protectorUserNo;
    private long protegeUserNo;
    private String message;
}
