package com.shinhan.knockknock.domain.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "card_tb")
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "card_id")
    private Long cardId;

    @Column(name= "card_no", length = 50)
    @NotNull
    private String cardNo;

    @Column(name= "card_cvc")
    @NotNull
    private String cardCvc; // 001 의 경우 1로 취급되기 때문에 String으로 설정

    @Column(name= "card_ename", length = 50)
    @NotNull
    private String cardEname;

    @Column(name= "card_password")
    @NotNull
    private String cardPassword;

    @Column(name= "card_bank")
    @NotNull
    private String cardBank;

    @Column(name= "card_account")
    @NotNull
    private String cardAccount;

    @Column(name= "card_amountdate")
    @NotNull
    private String cardAmountDate;

    @Column(name= "card_expiredate")
    @NotNull
    private Date cardExpiredate;

    @Column(name= "cardissue_no")
    @NotNull
    private Long cardIssueNo;

    @Column(name= "user_no")
    @NotNull
    private Long userNo;

    @Column(name= "card_isfamily")
    @NotNull
    private boolean cardIsfamily;

    @Column(name= "card_address")
    @NotNull
    private String cardAddress;

    @Nullable
    @Column(name= "card_userkname")
    private String cardUserKname;

    @Nullable
    @Column(name= "card_userphone")
    private String cardUserPhone;
}
