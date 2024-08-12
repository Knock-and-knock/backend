package com.shinhan.knockknock.domain.entity;

import jakarta.persistence.*;
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

    @Column(name= "card_no", length = 50, nullable= false)
    private String cardNo;

    @Column(name= "card_cvc", nullable= false)
    private String cardCvc; // 001 의 경우 1로 취급되기 때문에 String으로 설정

    @Column(name= "card_ename", length = 50, nullable= false)
    private String cardEname;

    @Column(name= "card_password", nullable= false)
    private int cardPassword;

    @Column(name= "card_bank", nullable= false)
    private String cardBank;

    @Column(name= "card_account", nullable= false)
    private String cardAccount;

    @Column(name= "card_amountdate", nullable = false)
    private Date cardAmountDate;

    @Column(name= "card_expiredate", nullable= false)
    private Date cardExpiredate;

    @Column(name= "cardissue_no",nullable = false)
    private Long cardIssueNo;

    @Column(name= "user_no", nullable = false)
    private Long userNo;
}
