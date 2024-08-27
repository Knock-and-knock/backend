package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.match.CreateMatchRequest;
import com.shinhan.knockknock.domain.dto.match.CreateMatchResponse;
import com.shinhan.knockknock.domain.dto.match.DeleteMatchResponse;
import com.shinhan.knockknock.domain.dto.match.UpdateMatchRequest;
import com.shinhan.knockknock.service.match.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "2. 매칭", description = "매칭 API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/match")
public class MatchController {

    private final MatchService matchService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "매칭 조회", description = "매칭 정보 조회하는 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매칭 조회 성공"),
            @ApiResponse(responseCode = "404", description = "매칭 조회 실패")
    })
    @GetMapping()
    public ResponseEntity<CreateMatchResponse> readMatch() {
        try {
            CreateMatchResponse response = matchService.readMatch();
            response.setMessage("조회 성공");
            return ResponseEntity.status(200).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(CreateMatchResponse.builder()
                    .message("매칭 정보가 없습니다.")
                    .build());
        }
    }

    @Operation(summary = "매칭 요청", description = "보호자가 매칭 요청을 보내는 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매칭 요청 성공"),
            @ApiResponse(responseCode = "404", description = "매칭 요청 실패")
    })
    @PostMapping()
    public ResponseEntity<CreateMatchResponse> createMatch(@RequestBody CreateMatchRequest request) {
        String message = "";
        try {
            CreateMatchResponse response = matchService.createMatch(request);
            response.setMessage("매칭 요청 성공");
            return ResponseEntity.status(200).body(response);
        } catch (RuntimeException e) {
            message = e.getMessage();
        }

        return ResponseEntity.status(404).body(CreateMatchResponse.builder()
                .message(message)
                .build());
    }

    @Operation(summary = "매칭 응답", description = "피보호자가 요청에 대한 응답을 하는 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공"),
            @ApiResponse(responseCode = "404", description = "응답 실패")
    })
    @PutMapping()
    public ResponseEntity<CreateMatchResponse> updateMatch(@RequestBody UpdateMatchRequest request) {
        String message = "";
        try {
            CreateMatchResponse response = matchService.updateMatch(request);
            response.setMessage("응답 성공");
            return ResponseEntity.status(200).body(response);
        } catch (RuntimeException e) {
            message = e.getMessage();
        }

        return ResponseEntity.status(404).body(CreateMatchResponse.builder()
                .message(message)
                .build());
    }

    @Operation(summary = "매칭 중단", description = "피보호자가 매칭을 중단하는 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공"),
            @ApiResponse(responseCode = "404", description = "응답 실패")
    })
    @DeleteMapping("/{matchNo}")
    public ResponseEntity<DeleteMatchResponse> deleteMatch(@PathVariable("matchNo") Long matchNo) {
        DeleteMatchResponse response = new DeleteMatchResponse();
        try {
            matchService.deleteMatch(matchNo);
            response.setMessage("매칭이 중단되었습니다.");
            return ResponseEntity.status(200).body(response);
        } catch (RuntimeException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }
}
