package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.CreateCardIssueRequest;
import com.shinhan.knockknock.domain.dto.CreateCardIssueResponse;
import com.shinhan.knockknock.domain.dto.ReadCardResponse;
import com.shinhan.knockknock.service.CardIssueService;
import com.shinhan.knockknock.service.CardService;
import com.shinhan.knockknock.service.ClovaOCRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/card")
public class CardIssueRestController {

    @Autowired
    CardIssueService cardIssueService;
    @Autowired
    CardService cardService;
    @Autowired
    ClovaOCRService clovaOCRService;

    // 카드 발급
    @PostMapping("/apply")
    public CreateCardIssueResponse createCardIssue(@RequestBody CreateCardIssueRequest request) {
        CreateCardIssueResponse createCardIssueResponse = cardIssueService.createPostCardIssue(request);
        return createCardIssueResponse;
    }

    // 카드 조회
    @GetMapping("/read/{userNo}")
    public ReadCardResponse readDetail(@PathVariable("userNo") Long userNo) {
        ReadCardResponse readCardResponse = cardService.readGetCard(userNo);
        return readCardResponse;
    }

    // 신분증 인증 보류
    @PostMapping("/auth")
    public void ocrService() {
        clovaOCRService.ocrService();
    }

}
