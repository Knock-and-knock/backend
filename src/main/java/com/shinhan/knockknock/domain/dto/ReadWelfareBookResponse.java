package com.shinhan.knockknock.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadWelfareBookResponse {
    private Long welfareBookNo;
    private String userId;
    private Timestamp welfareBookStartDate;
    private Timestamp welfareBookEndDate;
    private boolean welfareBookIsCansle;
    private boolean welfareBookIsComplete;
    private Long welfareNo;
}
