package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.ConversationRoomRequest;
import com.shinhan.knockknock.domain.dto.ConversationRoomResponse;
import com.shinhan.knockknock.service.ConversationRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conversation-room")
public class ConversationRoomController {

    @Autowired
    ConversationRoomService conversationRoomService;

    @PostMapping("/{userNo}")
    public String createConversationRoom(@PathVariable long userNo) {
        Long id = conversationRoomService.createConversationRoom(userNo);
        return "ok";
    }

    @GetMapping("/")
    public List<ConversationRoomResponse> readAll() {
        return conversationRoomService.readAllConversationRoom();
    }

    @PutMapping("/")
    public void updateConversationRoom(ConversationRoomRequest request) {
        conversationRoomService.updateConversationRoom(request);
    }
}
