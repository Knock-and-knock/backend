package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.user.*;
import com.shinhan.knockknock.service.user.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "0. 인증", description = "인증 API")
@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "아이디/패스워드 로그인", description = "아이디/패스워드 로그인을 위한 api")
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
        log.info("🔑 ID/Password Login Success - UserNo : {}, UserType : {}", response.getUserNo(), response.getUserType());
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "비밀번호 로그인", description = "간편비밀번호 로그인을 위한 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 실패")
    })
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

    @Operation(summary = "생체 로그인", description = "생체 로그인을 위한 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 실패")
    })
    @PostMapping("/login/bio")
    public ResponseEntity<TokenResponse> bioLogin(@RequestBody BioLoginUserRequest request) {
        TokenResponse response = null;
        try {
            response = authService.bioLoginUser(request);
        } catch (Exception e) {
            response = TokenResponse.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(401).body(response);
        }
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "로그아웃", description = "로그아웃 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패")
    })
    @PostMapping("/logout")
    public ResponseEntity<UserValidationResponse> logout(@RequestHeader("Authorization") String header) {
        try {
            long userNo = jwtProvider.getUserNoFromHeader(header);
            authService.logoutUser(userNo);
            return ResponseEntity.status(200).header("Authorization", "").body(UserValidationResponse.builder()
                    .message("로그아웃 되었습니다.")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(UserValidationResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }
}
