package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.service.ConversationRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conversation-room")
public class ConversationRoomController {

    @Autowired
    ConversationRoomService conversationRoomService;

    @PostMapping("/{userId}")
    public String createConversationRoom(@PathVariable long userId){
        Long id = conversationRoomService.createConversationRoom(userId);
        return "ok";
    }
}
