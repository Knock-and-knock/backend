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
    private String protectorUserName;
    private String protegeUserName;
    private long protectorUserNo;
    private long protegeUserNo;
}
