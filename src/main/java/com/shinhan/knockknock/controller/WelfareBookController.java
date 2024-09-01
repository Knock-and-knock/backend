package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.welfarebook.CreateWelfareBookRequest;
import com.shinhan.knockknock.domain.dto.welfarebook.ReadWelfareBookResponse;
import com.shinhan.knockknock.domain.entity.MatchEntity;
import com.shinhan.knockknock.repository.MatchRepository;
import com.shinhan.knockknock.repository.UserRepository;
import com.shinhan.knockknock.service.welfarebook.WelfareBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/welfare-book")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "4. 복지 예약 내역", description = "복지 예약 목록 API")
public class WelfareBookController {

    private final WelfareBookService welfareBookService;

    private final JwtProvider jwtProvider;

    private final MatchRepository matchRepository;

    private final UserRepository userRepository;

    @Operation(summary = "복지 예약 전체 조회", description = "특정 사용자의 복지 예약 내역을 전부 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "복지 예약 조회 성공"),
            @ApiResponse(responseCode = "400", description = "복지 예약 조회 실패")
    })
    @GetMapping
    public ResponseEntity<?> readAllByUserNo(@RequestHeader("Authorization") String header) {
        Long userNo = jwtProvider.getUserNoFromHeader(header);

        try {
            // MatchEntity를 통해 보호자와 매칭된 사용자인지 확인
            MatchEntity match = matchRepository.findByUserProtectorOrUserProtege(userRepository.findById(userNo).get(), userRepository.findById(userNo).get())
                    .orElse(null);

            // 만약 매칭이 되어있다면 보호자가 호출한 것이므로 userNo를 매칭된 일반 사용자의 userNo로 설정
            if (match != null && match.getUserProtector().getUserNo().equals(userNo)) {
                userNo = match.getUserProtege().getUserNo();
            }

            // 사용자의 복지 예약 내역 조회
            List<ReadWelfareBookResponse> welfareBooks = welfareBookService.readAllByUserNo(userNo);
            return ResponseEntity.ok(welfareBooks);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보가 존재하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("복지 예약 내역 조회 중 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "복지 예약 조회 detail [Not Use]", description = "복지 예약 내역 중 하나를 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "복지 예약 상세 조회 성공"),
            @ApiResponse(responseCode = "400", description = "복지 예약 상세 조회 실패")
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

    @Operation(summary = "복지 예약 하기", description = "일반 사용자와 보호자가 복지 서비스를 예약하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "복지 예약 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청, 입력된 값이 없음"),
            @ApiResponse(responseCode = "500", description = "복지 예약 생성 실패")
    })
    @PostMapping("/reserve")
    public ResponseEntity<?> createWelfareBooking(
            @RequestHeader("Authorization") String header,
            @Valid @RequestBody CreateWelfareBookRequest request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입력된 값이 없습니다.");
        }

        Long userNo = jwtProvider.getUserNoFromHeader(header);  // JWT 토큰에서 가져온 사용자 userNo

        try {
            // MatchEntity를 통해 보호자와 매칭된 사용자인지 확인
            MatchEntity match = matchRepository.findByUserProtectorOrUserProtege(userRepository.findById(userNo).get(), userRepository.findById(userNo).get())
                    .orElse(null);

            // 만약 매칭이 되어있다면 보호자가 호출한 것이므로 userNo를 매칭된 일반 사용자의 userNo로 설정
            if (match != null && match.getUserProtector().getUserNo().equals(userNo)) {
                userNo = match.getUserProtege().getUserNo();
            }

            Long welfareBookNo = welfareBookService.createWelfareBook(request, userNo);
            return ResponseEntity.status(HttpStatus.CREATED).body(welfareBookNo);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 또는 복지 항목이 존재하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("복지 예약 생성 중 오류가 발생했습니다.");
        }
    }


    @Operation(summary = "복지 예약 취소", description = "복지 서비스 예약을 취소하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "복지 예약 취소 성공"),
            @ApiResponse(responseCode = "400", description = "복지 예약 취소 실패")
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

