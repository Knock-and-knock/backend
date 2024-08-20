package com.shinhan.knockknock.domain.dto;

import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.WelfareEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "2024-08-08 09:00")
    private Timestamp welfareBookStartDate;
    @Schema(example = "2024-08-08 13:00")
    private Timestamp welfareBookEndDate;
    @Schema(example = "")
    private boolean welfareBookIsCansle;
    @Schema(example = "")
    private boolean welfareBookIsComplete;
    @Schema(example = "1")
    private UserEntity userNo;
    @Schema(example = "1")
    private WelfareEntity welfareNo;
}
