package com.shinhan.knockknock.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cardcategory_tb")
public class CardCategoryEntity {

    @Id
    @Column(name= "cardcategory_no")
    private Long cardCategoryNo;

    @Column(name= "cardcategory_name")
    private String cardCategoryName;
}
