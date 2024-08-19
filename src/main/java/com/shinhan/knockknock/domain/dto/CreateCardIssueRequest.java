package com.shinhan.knockknock.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardIssueRequest {
    private Long cardIssueNo;
    private String cardIssueResidentNo;
    private String cardIssueEname;
    private String cardIssueEmail;
    private String cardIssueBank;
    private String cardIssueAccount;
    private boolean cardIssueIsAgree;
    private int cardIssueIncome;
    private int cardIssueCredit;
    private Date cardIssueAmountDate;
    private String cardIssueSource;
    private boolean cardIssueIsHighrisk;
    private String cardIssuePurpose;
    private Timestamp cardIssueIssueDate;
    private Long userNo;
    private String cardIssueAddress;
    private boolean cardIssueIsFamily;
    private String cardIssuePassword;
}
