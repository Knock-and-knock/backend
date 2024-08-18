package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.CreateMatchRequest;
import com.shinhan.knockknock.domain.dto.CreateMatchResponse;
import com.shinhan.knockknock.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "매칭", description = "매칭 api")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/match")
public class MatchController {

    private final MatchService matchService;

    @Operation(summary = "매칭하기", description = "보호자가 매칭 요청을 보내는 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매칭 요청 성공")
    })
    @PostMapping()
    public ResponseEntity<CreateMatchResponse> match(@RequestBody CreateMatchRequest request) {
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
}
