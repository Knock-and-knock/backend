package com.shinhan.knockknock.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    @Schema(example = "1950-05-23")
    private Date userBirth;
    @Schema(example = "0")
    private int userGender;
    @Schema(example = "168")
    private int userHeight;
    @Schema(example = "66")
    private int userWeight;
    @Schema(example = "당뇨, 고혈압")
    private String userDisease;
    @Schema(example = "경기도 성남시 분당구 정자일로 95")
    private String userAddress;
    @Schema(example = "101동 204호")
    private String userAddressDetail;
}
