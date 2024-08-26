package com.shinhan.knockknock.domain.dto.conversation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WelfareBookInfoDto {
    private Timestamp welfareBookStartDate;
    private Timestamp welfareBookEndDate;
    private boolean welfareBookIsCansle;
    private boolean welfareBookIsComplete;
    private String welfareName;
    private Long welfarePrice;
    private Long welfareBookUseTime;
    private Integer welfareTotalPrice;
    private Timestamp welfareBookReservationDate;
}
