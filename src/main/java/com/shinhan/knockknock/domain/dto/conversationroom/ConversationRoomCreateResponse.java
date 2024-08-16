package com.shinhan.knockknock.domain.dto.conversationroom;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ConversationRoomCreateResponse {

    private Long conversationNo;
}
