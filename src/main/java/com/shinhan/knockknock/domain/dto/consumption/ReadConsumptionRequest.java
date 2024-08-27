package com.shinhan.knockknock.domain.dto.consumption;

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
public class ReadConsumptionRequest {
    @Schema(example = "24-08-01")
    private Date startDate;
    @Schema(example = "24-08-31")
    private Date endDate;
}
