package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.CreateCardIssueRequest;
import com.shinhan.knockknock.domain.dto.CreateCardIssueResponse;
import com.shinhan.knockknock.domain.dto.ReadCardResponse;
import com.shinhan.knockknock.service.CardIssueService;
import com.shinhan.knockknock.service.CardService;
import com.shinhan.knockknock.service.ClovaOCRService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/card")
public class CardController {

    @Autowired
    CardIssueService cardIssueService;
    @Autowired
    CardService cardService;
    @Autowired
    ClovaOCRService clovaOCRService;

    @Operation(summary = "카드 발급", description= "")
    @PostMapping("/apply")
    public CreateCardIssueResponse createPersonalCardIssue(@RequestBody CreateCardIssueRequest request) {
        CreateCardIssueResponse createCardIssueResponse = cardIssueService.createPostCardIssue(request);
        return createCardIssueResponse;
    }

    @Operation(summary = "본인 카드 조회", description ="발급 신청 후 비동기로 발급되어 1분 후 조회 가능")
    @GetMapping("/read/{userNo}")
    public List<ReadCardResponse> readDetail(@PathVariable("userNo") Long userNo) {
        return (List<ReadCardResponse>)cardService.readGetCards(userNo);
    }

    @Operation(summary = "신분증 인증 보류")
    @PostMapping("/auth")
    public void ocrService() {
        clovaOCRService.ocrService();
    }

}
