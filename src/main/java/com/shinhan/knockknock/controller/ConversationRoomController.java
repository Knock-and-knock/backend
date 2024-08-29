package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.conversation.*;
import com.shinhan.knockknock.service.conversation.ConversationRoomService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conversation-room")
@Tag(name = "3. 말동무", description = "말동무 관련 API")
public class ConversationRoomController {

    private final ConversationRoomService conversationRoomService;

    private final JwtProvider jwtProvider;

    @PostMapping
    @Operation(summary = "대화방 생성", description = "로그인 한 유저의 대화방을 생성합니다.")
    public ConversationRoomCreateResponse createConversationRoom(@RequestHeader("Authorization") String header) {
        Long userNo = jwtProvider.getUserNoFromHeader(header);
        Long roomNo = conversationRoomService.createConversationRoom(userNo);
        return ConversationRoomCreateResponse.builder().conversationRoomNo(roomNo).build();
    }

    @GetMapping
    @Operation(summary = "모든 대화방 조회 [Not Use]", description = "모든 대화방을 조회합니다.")
    @Hidden
    public List<ConversationRoomResponse> readAll() {
        return conversationRoomService.readAllConversationRoom();
    }

    @GetMapping("/last-conversation-time")
    @Operation(summary = "마지막 대화 시간 조회", description = "마지막으로 대화한 시간을 조회합니다.")
    public ConversationTimeResponse readLastConversationTime(@RequestHeader("Authorization") String header) {
        long userNo = jwtProvider.getUserNoFromHeader(header);
        Timestamp lastConversationTime = conversationRoomService.readLastConversationTime(userNo);

        return ConversationTimeResponse.builder()
                .conversationEndAt(lastConversationTime)
                .build();
    }

    @PutMapping("/{conversationRoomNo}")
    @Operation(summary = "대화방 수정 [Not Use]", description = "특정 대화방을 수정합니다.")
    @Hidden
    public ResponseEntity<MessageResponse> updateConversationRoom(@PathVariable long conversationRoomNo, @RequestBody ConversationRoomUpdateRequest request) {
        conversationRoomService.updateConversationRoom(conversationRoomNo, request);
        return ResponseEntity.ok(MessageResponse.builder().message("The conversation room has been successfully updated.").build());
    }

    @DeleteMapping("/{conversationRoomNo}")
    @Operation(summary = "대화방 삭제 [Not Use]", description = "특정 대화방을 삭제합니다.")
    @Hidden
    public ResponseEntity<MessageResponse> deleteConversationRoom(@PathVariable long conversationRoomNo) {
        conversationRoomService.deleteConversation(conversationRoomNo);
        return ResponseEntity.ok(MessageResponse.builder().message("The conversation room has been successfully deleted.").build());
    }
}
