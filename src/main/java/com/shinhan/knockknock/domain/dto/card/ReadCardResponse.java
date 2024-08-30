package com.shinhan.knockknock.domain.dto.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadCardResponse {
    private Long cardId;
    private String cardNo;
    private String cardBank;
    private boolean cardIsFamily;
    private String cardResponseMessage;
    private Long totalAmount;

}
