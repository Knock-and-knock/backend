package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRequest;
import com.shinhan.knockknock.service.ConsersationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/conversation")
@Tag(name = "말동무", description = "말동무 관련 API 입니다.")
public class ConversationController {

    @Autowired
    ConsersationService consersationService;
    @PostMapping
    public String conversation(ConversationRequest request) {
        consersationService.consersation(request);
        return "aa";
    }
}
