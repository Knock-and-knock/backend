package com.shinhan.knockknock.domain.dto.welfarebook;

import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.WelfareEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWelfareBookRequest {
    @Schema(example = "1")
    private Long welfareBookNo;
    @Schema(example = "2024-08-08 09:00")
    private Timestamp welfareBookStartDate;
    @Schema(example = "2024-08-08 13:00")
    private Timestamp welfareBookEndDate;
    @Schema(example = "")
    private boolean welfareBookIsCansle;
    @Schema(example = "")
    private boolean welfareBookIsComplete;
    @Schema(example = "1")
    private Long userNo;
    @Schema(example = "19550813")
    private Date userBirth;
    @Schema(example = "서울시 마포구 월드컵대로")
    private String userAddress;
    @Schema(example = "2")
    private int userGender;
    @Schema(example = "161")
    private int userHeight;
    @Schema(example = "54")
    private int userWeight;
    @Schema(example = "고혈압")
    private String userDisease;
    @Schema(example = "가정 돌봄")
    private String welfareName;
    @Schema(example = "6000")
    private Long welfarePrice;
}
