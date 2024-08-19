package com.shinhan.knockknock.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadWelfareResponse {
    private Long welfareNo;
    private String welfareName;
    private long welfarePirce;
    private String welfareCategory;
}
