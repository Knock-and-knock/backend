package com.shinhan.knockknock.domain.dto.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
public class CreateCardIssueResponse {

    String message;
    HttpStatus status;
}
