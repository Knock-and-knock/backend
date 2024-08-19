package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.CreateCardIssueRequest;
import com.shinhan.knockknock.domain.dto.CreateCardIssueResponse;
import com.shinhan.knockknock.domain.dto.ReadCardResponse;
import com.shinhan.knockknock.service.CardIssueService;
import com.shinhan.knockknock.service.CardService;
import com.shinhan.knockknock.service.ClovaOCRService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

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

    /*
    200 OK: 서버가 요청을 성공적으로 처리했으며, 그 결과를 클라이언트에게 반환하고 있다는 것을 의미
    202 ACCEPTED: 서버가 요청을 수신했으며, 이를 처리할 것임을 의미(비동기)
    따라서 비동기 작업 수행시 202로 변경 (test 위해서)
     */
    @Operation(summary = "카드 발급", description= "발급 신청 폼 받고 1분 후 카드 발급")
    @PostMapping("/apply")
    @ResponseStatus(HttpStatus.ACCEPTED) // 비동기 테스트 위해서 ACCEPTED로 가도록 추가
    public CreateCardIssueResponse createCardIssue(@RequestBody CreateCardIssueRequest request) {
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
