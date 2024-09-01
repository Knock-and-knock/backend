package com.shinhan.knockknock.domain.dto.card;

import com.shinhan.knockknock.domain.entity.RiskEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadCardIssueResponse {
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
    private String cardIssueIsFamily;
}