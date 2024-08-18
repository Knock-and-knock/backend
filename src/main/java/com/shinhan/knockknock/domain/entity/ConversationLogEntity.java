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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "conversationlog_tb")
public class ConversationLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversationlog_no")
    private long conversationLogNo;

    @Column(name = "conversationlog_input", columnDefinition = "TEXT")
    private String conversationLogInput;

    @Column(name = "conversationlog_response", columnDefinition = "TEXT")
    private String conversationLogResponse;

    @Column(name = "conversationlog_token")
    private Integer conversationLogToken;

    @Column(name = "conversationlog_datetime")
    @CreationTimestamp
    private Timestamp conversationLogDatetime;

    @ManyToOne
    @JoinColumn(name = "conversationroom_no", nullable = false)
    private ConversationRoomEntity conversationRoom;
}
