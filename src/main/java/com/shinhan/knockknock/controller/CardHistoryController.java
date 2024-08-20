package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.CreateCardCategoryRequest;
import com.shinhan.knockknock.domain.dto.CreateCardHistoryRequest;
import com.shinhan.knockknock.domain.dto.ReadCardHistoryResponse;
import com.shinhan.knockknock.service.CardCategoryService;
import com.shinhan.knockknock.service.CardHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/card-history")
@RequiredArgsConstructor
@Tag(name = "카드 내역", description = "카드 내역 목록 API")
public class CardHistoryController {

    final CardHistoryService cardHistoryService;
    final CardCategoryService cardCategoryService;

    @Operation(summary = "카드 내역 전체 조회", description = "카드 사용 내역을 전부 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카드 내역 조회 성공"),
            @ApiResponse(responseCode = "404", description = "카드 내역이 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<?> readAll() {
        try {
            List<ReadCardHistoryResponse> cardHistories = cardHistoryService.readAll();
            return ResponseEntity.ok(cardHistories);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카드 내역 조회 중 서버 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "카드 내역 생성", description = "카드 사용시 내역을 생성하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "카드 내역 생성 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 카드 내역 생성 실패")
    })
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateCardHistoryRequest request) {
        try {
            Long cardHistoryNo = cardHistoryService.createCardHistory(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(cardHistoryNo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카드 내역 생성 중 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "카드 내역 카테고리 수정", description = "카드 내역 중 카테고리를 수정하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카드 카테고리 수정 성공"),
            @ApiResponse(responseCode = "404", description = "해당 카드 카테고리가 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 수정 실패")
    })
    @PutMapping(consumes = "application/json;charset=utf-8", produces = "text/plain;charset=utf-8")
    public ResponseEntity<?> update(@RequestBody CreateCardCategoryRequest request) {
        try {
            cardCategoryService.updateCardCategory(request);
            return ResponseEntity.ok("카드 카테고리가 성공적으로 수정되었습니다.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카드 카테고리 수정 중 오류가 발생했습니다.");
        }
    }
}
