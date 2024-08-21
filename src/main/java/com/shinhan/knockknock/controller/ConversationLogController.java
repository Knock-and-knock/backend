package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.conversationroom.ConversationLogRequest;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationLogResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.MessageResponse;
import com.shinhan.knockknock.service.conversation.ConversationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conversation-log")
@Tag(name = "대화 내역", description = "대화 내역 관련 API 입니다.")
public class ConversationLogController {

    @Autowired
    ConversationLogService conversationLogService;

    @PostMapping
    @Operation(summary = "대화 내역 생성 [Not Use]", description = "특정 대화방의 대화 내용을 추가합니다.")
    public String createConversationLog(@RequestBody ConversationLogRequest request) {
        long logNo = conversationLogService.createConversationLog(request);
        return "ok! " + logNo;
    }

    @GetMapping
    @Operation(summary = "모든 대화 내역 조회 [Not Use]", description = "모든 대화 내역을 조회합니다.")
    public List<ConversationLogResponse> getAllConversationLogs() {
        return conversationLogService.readAllConversationLog();
    }

    @PutMapping("/{conversationLogNo}")
    @Operation(summary = "대화 내역 수정 [In Progress]", description = "특정 대화 내역을 수정합니다.")
    public ResponseEntity<MessageResponse> updateConversationLog(@PathVariable long conversationLogNo, @RequestBody ConversationLogRequest request) {
        conversationLogService.updateConversationLog(conversationLogNo, request);
        return ResponseEntity.ok(MessageResponse.builder().message("The conversation log has been successfully updated.").build());
    }

    @DeleteMapping("/{conversationLogNo}")
    @Operation(summary = "대화 내역 삭제 [Not Use]", description = "특정 대화 내역을 삭제합니다.")
    public ResponseEntity<MessageResponse> deleteConversationLog(@PathVariable long conversationLogNo) {
        conversationLogService.deleteConversationLog(conversationLogNo);
        return ResponseEntity.ok(MessageResponse.builder().message("The conversation log has been successfully deleted.").build());
    }
}
