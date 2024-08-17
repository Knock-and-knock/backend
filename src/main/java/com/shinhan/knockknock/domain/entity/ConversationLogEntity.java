package com.shinhan.knockknock.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Builder
@Entity
@Table(name = "conversationlog_tb")
public class ConversationLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversationlog_no")
    private long conversationLogNo;

    @Column(name = "conversationlog_input")
    private String conversationLogInput;

    @Column(name = "conversationlog_response")
    private String conversationLogResponse;

    @Column(name = "conversationlog_token")
    private Integer conversationLogToken;

    @Column(name = "conversationlog_datetime")
    @CreationTimestamp
    private Timestamp conversationLogDatetime;

    @Column(name = "conversationroom_no")
    @NotNull
    private long conversationRoomNo;
}
