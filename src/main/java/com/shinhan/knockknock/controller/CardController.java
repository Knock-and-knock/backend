package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.card.*;
import com.shinhan.knockknock.service.card.CardIssueService;
import com.shinhan.knockknock.service.card.CardService;
import com.shinhan.knockknock.service.card.ClovaOCRService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/card")
@RequiredArgsConstructor
@Tag(name = "5. 카드", description = "카드 발급, 조회 API")
public class CardController {

    private final CardIssueService cardIssueService;
    private final CardService cardService;
    private final ClovaOCRService clovaOCRService;
    private final JwtProvider jwtProvider;

    /*
    200 OK: 서버가 요청을 성공적으로 처리했으며, 그 결과를 클라이언트에게 반환하고 있다는 것을 의미
    202 ACCEPTED: 서버가 요청을 수신했으며, 이를 처리할 것임을 의미(비동기)
    따라서 비동기 작업 수행시 202로 변경 (test 위해서)
     */
    @Operation(summary = "카드 발급", description= "발급 신청 폼 받고 1분 후 카드 발급")
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED) // 비동기 테스트 ACCEPTED
    public CreateCardIssueResponse createCardIssue(@RequestHeader("Authorization") String header , @RequestBody CreateCardIssueRequest request) {
        request.setCardIssueEname(cardIssueService.mergeName(request));
        request.setCardIssueAddress(cardIssueService.mergeAddress(request));
        CreateCardIssueResponse createCardIssueResponse = cardIssueService.createPostCardIssue(request, jwtProvider.getUserNoFromHeader(header));
        return createCardIssueResponse;
    }

    // 본인 카드 리스트 조회
    @Operation(summary = "본인 카드 리스트 조회", description = "userNo로 본인의 카드 리스트 조회")
    @GetMapping
    public List<ReadCardResponse> readList(@RequestHeader("Authorization") String header) {
        Long userNo = jwtProvider.getUserNoFromHeader(header);
        return (List<ReadCardResponse>)cardService.readGetCards(userNo);
    }

    // 본인 카드 하나의 현재 월의 총 소비내역 조회
    // readList에 붙여 넣어야 할듯 : DTO 합쳐서 하나의 userNo의 카드 리스트에 각각 cardId와 해당 월 총 소비금액을
    @Operation(summary = "본인 카드 단일 조회", description = "cardId로 단일 카드의 현재 월의 총 소비금액 조회")
    @GetMapping("/{cardId}")
    public ReadCardSingleResponse readDetail(@PathVariable("cardId") Long cardId) {
        return cardService.readgetCard(cardId);
    }

    @Operation(summary = "개인 카드 신청정보 조회", description = "추후에 가족카드를 발급하기 위해 사용할 저장된 개인카드 신청정보 조회")
    @GetMapping("/readInfo")
    public List<ReadCardIssueResponse> readIssueInfo(@RequestHeader("Authorization") String header) {
        return cardIssueService.readIssueInfo(jwtProvider.getUserNoFromHeader(header));
    }

    @Operation(summary = "신분증 인증 보류")
    @PostMapping("/auth")
    public void ocrService() {
        clovaOCRService.ocrService();
    }

}
