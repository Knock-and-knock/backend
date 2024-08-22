package com.shinhan.knockknock.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Builder
@Table(name = "Welfarebook_tb")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class WelfareBookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "welfarebook_no")
    private Long welfareBookNo;

    @Column(name = "welfarebook_startdate")
    @NotNull
    private Timestamp welfareBookStartDate;

    @Column(name = "welfarebook_enddate")
    @NotNull
    private Timestamp welfareBookEndDate;

    @Column(name = "welfarebook_iscansle")
    private boolean welfareBookIsCansle;

    @Column(name = "welfarebook_iscomplete")
    private boolean welfareBookIsComplete;

    @ManyToOne
    @JoinColumn(name = "user_no")
    @NotNull
    private UserEntity user;

    public Long getUserNo(){
        return user.getUserNo();
    }

    @ManyToOne
    @JoinColumn(name = "welfare_no")
    @NotNull
    private WelfareEntity welfare;

    public String getWelfareName(){
        return welfare.getWelfareName();
    }

    public Long getWelfarePrice(){
        return welfare.getWelfarePrice();
    }
}
