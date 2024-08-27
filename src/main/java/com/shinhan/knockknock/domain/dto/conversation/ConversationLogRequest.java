package com.shinhan.knockknock.domain.dto.conversation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationLogRequest {

    private String conversationLogInput;
    private String conversationLogResponse;
    private Integer conversationLogToken;
    private long conversationRoomNo;

}
