package com.shinhan.knockknock.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
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

    @Column(name= "cardissue_residentno", length = 50, nullable= false)
    private String cardIssueResidentNo;

    @Column(name= "cardissue_ename", length = 50, nullable= false)
    private String cardIssueEname;

    @Column(name= "cardissue_email", length = 50)
    private String cardIssueEmail;

    @Column(name= "cardissue_bank", length = 20, nullable= false)
    private String cardIssueBank;

    @Column(name= "cardissue_account", length = 50, nullable= false)
    private String cardIssueAccount;

    @Column(name= "cardissue_isagree", nullable= false)
    private boolean cardIssueIsAgree;

    @Column(name= "cardissue_income", nullable= false)
    private int cardIssueIncome;

    @Column(name= "cardissue_credit", nullable= false)
    private int cardIssueCredit;

    @Column(name= "cardissue_amountdate", nullable= false)
    private Date cardIssueAmountDate;

    @Column(name= "cardissue_source", nullable= false)
    private String cardIssueSource;

    @Column(name= "cardissue_ishighrisk", nullable= false)
    private boolean cardIssueIsHighrisk;

    @Column(name= "cardissue_purpose", nullable= false)
    private String cardIssuePurpose;

    @Column(name= "cardissue_issuedate")
    @CreationTimestamp
    private Timestamp cardIssueIssueDate;

    @Column(name= "user_no", nullable= false)
    private Long userNo;

}

