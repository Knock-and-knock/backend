package com.shinhan.knockknock.domain.dto;

import com.shinhan.knockknock.domain.entity.RiskEnum;
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
    //private String cardIssueResidentNo;
    private String cardIssueFirstEname;
    private String cardIssueLastEname;
    private String cardIssueEname;
    private String cardIssueEmail;
    private String cardIssueBank;
    private String cardIssueAccount;
    private boolean cardIssueIsAgree;
    private String cardIssueIncome;
    private String cardIssueCredit;
    private String cardIssueAmountDate;
    private String cardIssueSource;
    private RiskEnum cardIssueIsHighrisk;
    private String cardIssuePurpose;
    private String cardIssueAddress;
    private boolean cardIssueIsFamily;
    private String cardIssuePassword;
}
