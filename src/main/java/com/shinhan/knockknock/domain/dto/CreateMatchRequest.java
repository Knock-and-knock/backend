package com.shinhan.knockknock.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMatchRequest {
    private String protegeName;
    private String protegePhone;
    private String matchProtectorName;
    private String matchProtegeName;
    private long matchProtectorNo;
    private long matchProtegeNo;
}
