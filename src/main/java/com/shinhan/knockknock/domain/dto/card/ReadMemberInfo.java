package com.shinhan.knockknock.domain.dto.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadMemberInfo {
    private String userName;
    private String userPhone;
}
