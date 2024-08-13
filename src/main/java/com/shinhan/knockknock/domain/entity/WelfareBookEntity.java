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

    @Column(name = "user_id")
    @NotNull
    private String userId;

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

    @Column(name = "welfare_no")
    @NotNull
    private long welfareNo;
}
