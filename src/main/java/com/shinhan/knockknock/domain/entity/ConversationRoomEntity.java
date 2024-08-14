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
@Table(name = "conversationroom_tb")
public class ConversationRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversationroom_no")
    private long conversationRoomNo;

    @Column(name = "conversationroom_start")
    @CreationTimestamp
    private Timestamp conversationRoomStart;

    @Column(name = "conversationroom_end")
    private Timestamp conversationRoomEnd;

    @Column(name = "user_no")
    @NotNull
    private long UserNo;
}
