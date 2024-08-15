package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.LoginUserRequest;
import com.shinhan.knockknock.domain.dto.TokenResponse;
import com.shinhan.knockknock.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인", description = "아이디/패스워드, 간편비밀번호 로그인을 위한 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginUserRequest request) {
        TokenResponse response = null;
        try {
            if (request.getLoginType().equals("NORMAL")) {
                response = authService.loginUser(request);
            } else {
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            response = TokenResponse.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(401).body(response);
        }
        return ResponseEntity.status(200).body(response);
    }
}
