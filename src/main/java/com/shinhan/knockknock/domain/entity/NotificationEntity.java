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
@Table(name = "notification_tb")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationNo;

    @NotNull
    private String notificationCategory;
    private String notificationTitle;
    private String notificationContent;

    @CreationTimestamp
    @Column(name="notification_datetime")
    @NotNull
    private Timestamp notificationDateTime;

    @Column(name="notification_ischeck")
    @NotNull
    private boolean notificationIsCheck;
    private Long userNo;

}
