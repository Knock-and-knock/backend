package com.shinhan.knockknock.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Table(name = "Welfare_tb")
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Entity
public class WelfareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "welfare_no")
    private long welfareNo;
    @NotNull
    @Column(name = "welfare_name")
    private String welfareName;
    @Column(name = "welfare_price")
    private long welfarePrice;
    @Column(name = "welfare_category")
    private String welfareCategory;
}

