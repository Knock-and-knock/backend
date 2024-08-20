package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.IdLoginUserRequest;
import com.shinhan.knockknock.domain.dto.SimpleLoginUserRequest;
import com.shinhan.knockknock.domain.dto.TokenResponse;
import com.shinhan.knockknock.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증", description = "인증 API")
@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인 [In Progress]", description = "아이디/패스워드, 간편비밀번호 로그인을 위한 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 실패")
    })
    @PostMapping("/login/normal")
    public ResponseEntity<TokenResponse> idLogin(@RequestBody IdLoginUserRequest request) {
        TokenResponse response = null;
        try {
            response = authService.loginUser(request);
        } catch (Exception e) {
            response = TokenResponse.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(401).body(response);
        }
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/login/simple")
    public ResponseEntity<TokenResponse> simpleLogin(@RequestBody SimpleLoginUserRequest request) {
        TokenResponse response = null;
        try {
            response = authService.simpleLoginUser(request);
        } catch (Exception e) {
            response = TokenResponse.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(401).body(response);
        }
        return ResponseEntity.status(200).body(response);
    }
}
