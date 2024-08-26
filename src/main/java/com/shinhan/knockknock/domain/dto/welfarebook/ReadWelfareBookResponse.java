package com.shinhan.knockknock.domain.dto.welfarebook;

import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.WelfareEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadWelfareBookResponse {
    @Schema(example = "2024-08-08 09:00")
    private Date welfareBookStartDate;
    @Schema(example = "2024-08-08 13:00")
    private Date welfareBookEndDate;
    @Schema(example = "false")
    private boolean welfareBookIsCansle;
    @Schema(example = "true")
    private boolean welfareBookIsComplete;
    @Schema(example = "1")
    private Long userNo;
    @Schema(example = "가정 돌봄")
    private String welfareName;
    @Schema(example = "6000")
    private Long welfarePrice;
    @Schema(example = "3")
    private Integer welfareBookUseTime;

    private Integer welfareTotalPrice;

    private Timestamp welfareBookReservationDate;
}
