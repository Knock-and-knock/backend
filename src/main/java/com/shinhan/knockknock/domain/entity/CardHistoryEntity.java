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
@Table(name = "cardhistory_tb")
public class CardHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "cardhistory_no")
    private Long cardHistoryNo;

    @Column(name="cardhistory_amount")
    @NotNull
    private int cardHistoryAmount;

    @Column(name="cardhistory_shopname")
    @NotNull
    private String cardHistoryShopname;

    @Column(name="cardhistory_approve")
    @CreationTimestamp
    @NotNull
    private Timestamp cardHistoryApprove;

    @ManyToOne
    @JoinColumn(name= "cardcategory_no")
    @NotNull
    private CardCategoryEntity cardCategoryNo;

    @ManyToOne
    @JoinColumn(name= "card_id")
    @NotNull
    private CardEntity cardId;

}
