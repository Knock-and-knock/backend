package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.CreateMatchRequest;
import com.shinhan.knockknock.domain.dto.CreateMatchResponse;
import com.shinhan.knockknock.domain.dto.UpdateMatchRequest;
import com.shinhan.knockknock.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "매칭", description = "매칭 api")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/match")
public class MatchController {

    private final MatchService matchService;

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
}
