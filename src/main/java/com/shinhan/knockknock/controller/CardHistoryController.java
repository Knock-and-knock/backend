package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.cardcategory.CreateCardCategoryRequest;
import com.shinhan.knockknock.domain.dto.cardhistory.CreateCardHistoryRequest;
import com.shinhan.knockknock.domain.dto.cardhistory.ReadCardHistoryResponse;
import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.repository.CardRepository;
import com.shinhan.knockknock.service.cardcategory.CardCategoryService;
import com.shinhan.knockknock.service.cardhistory.CardHistoryService;
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

    final CardRepository cardRepository;
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

    @Operation(summary = "가족 카드 관련 사용자 조회", description = "가족 카드일 경우, 관련된 사용자의 이름을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공"),
            @ApiResponse(responseCode = "404", description = "관련 사용자를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 사용자 조회 실패")
    })
    @GetMapping("/{cardId}")
    public ResponseEntity<?> getFamilyCardUserName(@PathVariable("cardId") Long cardId) {
        try {
            // cardId를 사용하여 CardEntity를 조회
            CardEntity cardEntity = cardRepository.findById(cardId)
                    .orElseThrow(() -> new NoSuchElementException("해당 카드가 존재하지 않습니다."));

            // CardEntity를 사용하여 관련 사용자의 이름 조회
            String userName = cardHistoryService.findUserNameForFamilyCard(cardEntity);
            return ResponseEntity.ok(userName);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 조회 중 오류가 발생했습니다.");
        }
    }
}
