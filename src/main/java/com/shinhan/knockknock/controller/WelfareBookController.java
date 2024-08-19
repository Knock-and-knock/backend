package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.CreateWelfareBookRequest;
import com.shinhan.knockknock.domain.dto.ReadWelfareBookResponse;
import com.shinhan.knockknock.repository.WelfareBookRepository;
import com.shinhan.knockknock.service.WelfareBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/welfare-book")
@RequiredArgsConstructor
@Tag(name = "복지 예약 내역", description = "복지 예약 목록 API")
public class WelfareBookController {

    final WelfareBookService welfareBookService;

    @Operation(summary = "복지 예약 전체 조회", description = "복지 예약 내역을 전부 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "복지예약 조회 성공"),
            @ApiResponse(responseCode = "400", description = "복지예약 조회 실패")
    })
    @GetMapping
    public ResponseEntity<?> readAll() {
        try {
            List<ReadWelfareBookResponse> welfareBooks = welfareBookService.readAll();
            return ResponseEntity.ok(welfareBooks);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "복지 예약 조회 detail [Not Use]", description = "복지 예약 내역 중 하나를 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "복지예약 detail 조회 성공"),
            @ApiResponse(responseCode = "400", description = "복지예약 detail 조회 실패")
    })
    @GetMapping("/{welfareBookNo}")
    public ResponseEntity<?> readDetail(@PathVariable("welfareBookNo") Long welfareBookNo) {
        try {
            ReadWelfareBookResponse welfareBook = welfareBookService.readDetail(welfareBookNo);
            return ResponseEntity.ok(welfareBook);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "복지 예약 하기", description = "복지서비스를 예약하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "복지예약 생성 성공"),
            @ApiResponse(responseCode = "400", description = "복지예약 생성 실패")
    })
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CreateWelfareBookRequest request) {
        try {
            Long welfareBookNo = welfareBookService.createWelfareBook(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(welfareBookNo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "복지 예약 취소", description = "복지서비스 예약을 취소하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "복지예약 취소 성공"),
            @ApiResponse(responseCode = "400", description = "복지예약 취소 실패")
    })
    @DeleteMapping("/{welfareBookNo}")
    public ResponseEntity<String> delete(@PathVariable("welfareBookNo") Long welfareBookNo) {
        try {
            welfareBookService.deleteWelfareBook(welfareBookNo);
            return ResponseEntity.ok("복지 예약이 성공적으로 취소되었습니다.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("복지 예약 취소에 실패했습니다.");
        }
    }
}
