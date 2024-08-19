package com.shinhan.knockknock.domain.entity;

import jakarta.persistence.*;
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
@Table(name = "cardhistory_tb")
public class CardHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "cardhistory_no")
    private Long cardHistoryNo;

    @Column(name="cardhistory_amount")
    private int cardHistoryAmount;

    @Column(name="cardhistory_shopname")
    private String cardHistoryShopname;

    @Column(name="cardhistory_approve")
    @CreationTimestamp
    private Timestamp cardHistoryApprove;

    @Column(name= "cardcategory_no")
    private Long cardCategoryNo;

    @Column(name= "card_id")
    private Long cardId;

}
