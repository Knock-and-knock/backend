package com.shinhan.knockknock.domain.dto.conversation;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ConversationRoomResponse {

    private Long conversationNo;
    private Timestamp conversationStartAt;
    private Timestamp conversationEndAt;
    private Long userNo;

}
