package com.shinhan.knockknock.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "conversationroom_tb")
public class ConversationRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversationroom_no")
    private long conversationRoomNo;

    @Column(name = "conversationroom_start_at")
    @CreationTimestamp
    private Timestamp conversationRoomStartAt;

    @Column(name = "conversationroom_end_at")
    private Timestamp conversationRoomEndAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "conversationRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ConversationLogEntity> conversationLogs;

    @Override
    public String toString() {
        return "ConversationRoomEntity{" +
                "conversationRoomNo=" + conversationRoomNo +
                ", conversationRoomStartAt=" + conversationRoomStartAt +
                ", conversationRoomEndAt=" + conversationRoomEndAt +
                ", userNo=" + (user != null ? user.getUserNo() : "null") +
                '}';
    }
}
