package com.shinhan.knockknock.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
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
    private Date welfareBookStartDate;

    @Column(name = "welfarebook_enddate")
    @NotNull
    private Date welfareBookEndDate;

    @Column(name = "welfarebook_iscansle")
    private boolean welfareBookIsCansle;

    @Column(name = "welfarebook_iscomplete")
    private boolean welfareBookIsComplete;

    @Column(name = "welfarebook_usetime")
    private Integer welfareBookUseTime;

    @Column(name = "welfarebook_totalprice")
    private Integer welfareTotalPrice;

    @Column(name = "welfarebook_reservationdate")
    @CreationTimestamp
    @NotNull
    private Timestamp welfareBookReservationDate;

    @ManyToOne
    @JoinColumn(name = "user_no")
    @NotNull
    private UserEntity user;

    public Long getUserNo(){
        return user.getUserNo();
    }

    public Date getUserBirth(){ return user.getUserBirth();}

    public String getUserAddress(){ return user.getUserAddress();}

    public int getUserGender(){ return user.getUserGender();}

    public int getUserHeight(){ return user.getUserHeight();}

    public int getUserWeight(){ return user.getUserWeight();}

    public String getUserDisease(){ return user.getUserDisease();}

    @ManyToOne
    @JoinColumn(name = "welfare_no")
    @NotNull
    private WelfareEntity welfare;

    public Long getWelfareNo(){ return welfare.getWelfareNo();}

    public String getWelfareName(){
        return welfare.getWelfareName();
    }

    public Long getWelfarePrice(){
        return welfare.getWelfarePrice();
    }

}
