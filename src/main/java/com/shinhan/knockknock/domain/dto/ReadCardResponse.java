package com.shinhan.knockknock.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadCardResponse {
    //private Long cardId;
    private String cardNo;
    private String cardCvc;
    private String cardEname;
    //private int cardPassword;
    private String cardBank;
    private String cardAccount;
    private String cardAmountDate;
    private String cardExpiredate;
    //private Long issueNo;
    //private Long userNo;
    private String cardAddress;
    private boolean cardIsFamily;
    private String cardResponseMessage;
}
