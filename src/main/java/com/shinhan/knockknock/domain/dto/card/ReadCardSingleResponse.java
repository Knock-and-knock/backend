package com.shinhan.knockknock.domain.dto.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadCardSingleResponse {
    private Long cardId;
    private Long totalAmount;
}
