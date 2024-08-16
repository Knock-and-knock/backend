package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.ConversationRoomRequest;
import com.shinhan.knockknock.domain.dto.ConversationRoomResponse;
import com.shinhan.knockknock.service.ConversationRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conversation-room")
@Tag(name = "대화방", description = "대화 방 관련 API 입니다.")
public class ConversationRoomController {

    @Autowired
    ConversationRoomService conversationRoomService;

    @PostMapping("/{userNo}")
    @Operation(summary = "대화방 생성", description = "특정 유저의 대화방을 생성합니다.")
    public String createConversationRoom(@PathVariable long userNo) {
        Long id = conversationRoomService.createConversationRoom(userNo);
        return "ok";
    }

    @GetMapping("/")
    @Operation(summary = "모든 대화방 조회", description = "모든 대화방을 조회합니다.")
    public List<ConversationRoomResponse> readAll() {
        return conversationRoomService.readAllConversationRoom();
    }

    @PutMapping("/")
    @Operation(summary = "대화방 수정 [In Progress]", description = "특정 대화방을 수정합니다.")
    public void updateConversationRoom(ConversationRoomRequest request) {
        conversationRoomService.updateConversationRoom(request);
    }
}
