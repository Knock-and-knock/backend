package com.shinhan.knockknock.domain.dto.conversationroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationLogResponse {

    private  long conversationLogNo;
    private String conversationLogInput;
    private String conversationLogResponse;
    private Integer conversationLogToken;
    private Timestamp conversationLogDatetime;
    private long conversationRoomNo;

}
