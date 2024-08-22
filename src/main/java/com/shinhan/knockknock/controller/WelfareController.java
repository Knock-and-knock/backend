package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.welfare.CreateWelfareRequest;
import com.shinhan.knockknock.domain.dto.welfare.ReadWelfareResponse;
import com.shinhan.knockknock.repository.WelfareRepository;
import com.shinhan.knockknock.service.welfare.WelfareService;
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
@RequestMapping("api/v1/welfare")
@RequiredArgsConstructor
@Tag(name = "복지 목록", description = "복지 목록 API")
public class WelfareController {

    final WelfareRepository welfareRepo;
    final WelfareService welfareService;

    @Operation(summary = "복지 목록 전체 조회", description = "복지 목록을 전부 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "복지목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "복지 목록이 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 복지목록 조회 실패")
    })
    @GetMapping
    public ResponseEntity<?> readAll() {
        try {
            List<ReadWelfareResponse> welfareList = welfareService.readAll();
            return ResponseEntity.ok(welfareList);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("복지 목록 조회 중 서버 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "복지 생성 [Not Use]", description = "복지서비스를 생성하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "복지목록 생성 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 복지목록 생성 실패")
    })
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CreateWelfareRequest request) {
        try {
            Long welfareNo = welfareService.createWelfare(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(welfareNo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "복지 수정 [Not Use]", description = "복지서비스를 수정하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "복지목록 수정 성공"),
            @ApiResponse(responseCode = "404", description = "해당 복지 서비스가 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 복지목록 수정 실패")
    })
    @PutMapping(consumes = "application/json;charset=utf-8", produces = "text/plain;charset=utf-8")
    public ResponseEntity<String> update(@RequestBody CreateWelfareRequest request) {
        try {
            welfareService.updateWelfare(request);
            return ResponseEntity.ok("복지 서비스가 성공적으로 수정되었습니다.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("복지 서비스 수정 중 서버 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "복지 삭제 [Not Use]", description = "복지서비스를 삭제하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "복지목록 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 복지 서비스가 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 복지목록 삭제 실패")
    })
    @DeleteMapping("/{welfareNo}")
    public ResponseEntity<String> delete(@PathVariable("welfareNo") Long welfareNo) {
        try {
            welfareService.deleteWelfare(welfareNo);
            return ResponseEntity.ok("복지 서비스가 성공적으로 삭제되었습니다.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("복지 서비스 삭제 중 서버 오류가 발생했습니다.");
        }
    }
}
