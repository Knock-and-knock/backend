package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.cardcategory.CreateCardCategoryRequest;
import com.shinhan.knockknock.domain.dto.cardhistory.CreateCardHistoryRequest;
import com.shinhan.knockknock.domain.dto.cardhistory.DetailCardHistoryResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/card-history")
@RequiredArgsConstructor
@Tag(name = "6. 카드 내역", description = "카드 내역 목록 API")
@Slf4j
public class CardHistoryController {

    final CardRepository cardRepository;
    final CardHistoryService cardHistoryService;
    final CardCategoryService cardCategoryService;

    @Operation(summary = "카드 내역 조회", description = "특정 카드의 사용 내역을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카드 내역 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<ReadCardHistoryResponse>> readAll(@RequestParam("cardId") Long cardId,
                                                                   @RequestParam(value = "startDate", required = false) String startDateStr,
                                                                   @RequestParam(value = "endDate", required = false) String endDateStr) {
        try {
            List<ReadCardHistoryResponse> cardHistories;

            if (startDateStr != null && endDateStr != null) {
                // 날짜만 입력된 경우, 시간을 기본값으로 설정
                LocalDate startDate = LocalDate.parse(startDateStr);
                LocalDate endDate = LocalDate.parse(endDateStr);

                // startDate는 00:00:00, endDate는 23:59:59로 설정
                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

                // Timestamp로 변환
                Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
                Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

                cardHistories = cardHistoryService.readAllWithinDateRange(cardId, startTimestamp, endTimestamp);
            } else {
                cardHistories = cardHistoryService.readAll(cardId);
            }

            return ResponseEntity.ok(cardHistories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "카드 내역 조회 Detail", description = "특정 카드의 사용 내역을 Detail조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카드 내역 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{cardHistoryNo}")
    public DetailCardHistoryResponse readDetail(@PathVariable("cardHistoryNo") Long cardHistoryNo){
        return cardHistoryService.readDetail(cardHistoryNo);
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
            System.out.println(e);
            log.info(String.valueOf(e));
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

    @Operation(summary = "가족 카드 관련 사용자 조회[Not Use]", description = "가족 카드일 경우, 관련된 사용자의 이름을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공"),
            @ApiResponse(responseCode = "404", description = "관련 사용자를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 사용자 조회 실패")
    })
    @GetMapping("/username/{cardId}")
    public ResponseEntity<?> getFamilyCardUserName(@RequestBody@PathVariable("cardId") Long cardId) {
        try {
            CardEntity cardEntity = cardRepository.findById(cardId)
                    .orElseThrow(() -> new NoSuchElementException("해당 카드가 존재하지 않습니다."));

            String userName = cardHistoryService.findUserNameForFamilyCard(cardEntity);
            return ResponseEntity.ok(userName);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 조회 중 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "카드내역 취소", description = "카드내역을 취소할 때 사용하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카드 내역 취소 성공"),
            @ApiResponse(responseCode = "404", description = "해당 카드 내역을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 취소 실패")
    })
    @PutMapping("/{cardHistoryNo}")
    public ResponseEntity<String> cancelCardHistory(@PathVariable("cardHistoryNo") Long cardHistoryNo) {
        try {
            cardHistoryService.cancelCardHistory(cardHistoryNo);
            return ResponseEntity.ok("카드 내역이 성공적으로 취소되었습니다.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카드 내역 취소 중 오류가 발생했습니다.");
        }
    }
}


