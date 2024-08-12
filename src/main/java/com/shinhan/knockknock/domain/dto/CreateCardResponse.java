package com.shinhan.knockknock.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
public class CreateCardResponse {
    //private Long cardId;
    private String cardNo;
    private String cardCvc;
    private String cardEname;
    //private int cardPassword;
    private String cardBank;
    private String cardAccount;
    private Date cardAmountDate;
    private Date cardExpiredate;
    //private Long issueNo;
    //private Long userNo;
}
