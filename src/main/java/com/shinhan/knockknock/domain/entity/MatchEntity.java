package com.shinhan.knockknock.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "match_tb")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_no")
    private Long matchNo;

    @Column(name = "match_protectorname")
    private String matchProtectorName;

    @Column(name = "match_protegename")
    private String matchProtegeName;

    @Column(name = "match_status")
    @NotNull
    @Builder.Default
    private String matchStatus = "WAIT";    // WAIT(대기), ACCEPT(수락), REJECT(거절)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_protectorno")
    @NotNull
    private UserEntity userProtector;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_protegeno")
    @NotNull
    private UserEntity userProtege;
}
