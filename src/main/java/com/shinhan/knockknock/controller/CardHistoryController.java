package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.CreateCardCategoryRequest;
import com.shinhan.knockknock.domain.dto.CreateCardHistoryRequest;
import com.shinhan.knockknock.domain.dto.ReadCardHistoryResponse;
import com.shinhan.knockknock.domain.entity.CardHistoryEntity;
import com.shinhan.knockknock.repository.CardHistoryRepository;
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

    final CardHistoryRepository cardHistoryRepo;
    final CardHistoryService cardHistoryService;
    final CardCategoryService cardCategoryService;

    @Operation(summary = "카드 내역 전체 조회", description = "카드 사용 내역을 전부 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카드내역 조회 성공"),
            @ApiResponse(responseCode = "404", description = "카드내역이 존재하지 않습니다.")
    })
    @GetMapping
    public ResponseEntity<?> readAll() {
        try {
            List<ReadCardHistoryResponse> cardHistories = cardHistoryService.readAll();
            return ResponseEntity.ok(cardHistories);
        } catch (NoSuchElementException e) {
            // 조회 결과가 없을 때 404 상태 코드로 응답
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "카드 내역 생성", description = "카드 사용시 내역을 생성하는 API입니다.")
    @PostMapping
    Long create(@RequestBody CreateCardHistoryRequest request){
        return cardHistoryService.createCardHistory(request);
    }

    @Operation(summary = "카드 내역 카테고리 수정", description = "카드 내역중 카테고리를 수정하는 API입니다.")
    @PutMapping(consumes = "application/json;charset=utf-8", produces = "text/plain;charset=utf-8")
    void update(@RequestBody CreateCardCategoryRequest request){
        cardCategoryService.updateCardCategory(request);
    }
}
