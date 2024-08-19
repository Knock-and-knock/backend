package com.shinhan.knockknock.domain.dto;

import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.WelfareEntity;
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
    //private Long welfareBookNo;
    private Timestamp welfareBookStartDate;
    private Timestamp welfareBookEndDate;
    private boolean welfareBookIsCansle;
    private boolean welfareBookIsComplete;
    private UserEntity userNo;
    private WelfareEntity welfareNo;
}
