package com.shinhan.knockknock.domain.dto.conversation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConversationRoomCreateResponse {

    private Long conversationRoomNo;
}