package com.shinhan.knockknock.domain.dto.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadIsCardResponse {
    @JsonProperty("isCard")
    private boolean isCard;

}
