package com.shinhan.knockknock.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cardissue_tb")
public class CardIssueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "cardissue_no")
    private Long cardIssueNo;

    @Column(name= "cardissue_ename", length = 50)
    @NotNull
    private String cardIssueEname;

    @Column(name= "cardissue_email", length = 50)
    private String cardIssueEmail;

    @Column(name= "cardissue_bank", length = 20)
    @NotNull
    private String cardIssueBank;

    @Column(name= "cardissue_account", length = 50)
    @NotNull
    private String cardIssueAccount;

    @Column(name= "cardissue_isagree")
    @NotNull
    private boolean cardIssueIsAgree;

    @Column(name= "cardissue_income", length = 50)
    @NotNull
    private String cardIssueIncome;

    @Column(name= "cardissue_credit", length = 50)
    @NotNull
    private String cardIssueCredit;

    @Column(name= "cardissue_amountdate")
    @NotNull
    private String cardIssueAmountDate;

    @Column(name= "cardissue_source")
    @NotNull
    private String cardIssueSource;

    @Enumerated(EnumType.STRING)
    @Column(name= "cardissue_ishighrisk")
    @NotNull
    private RiskEnum cardIssueIsHighrisk;

    @Column(name= "cardissue_purpose")
    @NotNull
    private String cardIssuePurpose;

    @Column(name= "cardissue_issuedate", length = 50)
    @CreationTimestamp
    private Timestamp cardIssueIssueDate;

    @Column(name= "user_no")
    @NotNull
    private Long userNo;

    @Column(name= "cardissue_isfamily")
    @NotNull
    private boolean cardIssueIsFamily;

    @Column(name= "cardissue_address")
    @NotNull
    private String cardIssueAddress;

}

