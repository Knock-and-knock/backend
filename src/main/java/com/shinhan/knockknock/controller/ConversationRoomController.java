package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRoomCreateResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRoomResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRoomUpdateRequest;
import com.shinhan.knockknock.domain.dto.conversationroom.MessageResponse;
import com.shinhan.knockknock.service.conversation.ConversationRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/conversation-room")
@Tag(name = "대화방", description = "대화 방 관련 API")
public class ConversationRoomController {

    @Autowired
    ConversationRoomService conversationRoomService;

    @PostMapping
    @Operation(summary = "대화방 생성", description = "로그인 한 유저의 대화방을 생성합니다.")
    public ConversationRoomCreateResponse createConversationRoom() {
        long userNo = 1L;
        Long roomNo = conversationRoomService.createConversationRoom(userNo);
        return ConversationRoomCreateResponse.builder().conversationRoomNo(roomNo).build();
    }

    @GetMapping
    @Operation(summary = "모든 대화방 조회 [Not Use]", description = "모든 대화방을 조회합니다.")
    public List<ConversationRoomResponse> readAll() {
        return conversationRoomService.readAllConversationRoom();
    }

    @PutMapping("/{conversationRoomNo}")
    @Operation(summary = "대화방 수정 [Not Use]", description = "특정 대화방을 수정합니다.")
    public ResponseEntity<MessageResponse> updateConversationRoom(@PathVariable long conversationRoomNo, @RequestBody ConversationRoomUpdateRequest request) {
        conversationRoomService.updateConversationRoom(conversationRoomNo, request);
        return ResponseEntity.ok(MessageResponse.builder().message("The conversation room has been successfully updated.").build());
    }

    @DeleteMapping("/{conversationRoomNo}")
    @Operation(summary = "대화방 삭제 [Not Use]", description = "특정 대화방을 삭제합니다.")
    public ResponseEntity<MessageResponse> deleteConversationRoom(@PathVariable long conversationRoomNo) {
        conversationRoomService.deleteConversation(conversationRoomNo);
        return ResponseEntity.ok(MessageResponse.builder().message("The conversation room has been successfully deleted.").build());
    }
}
