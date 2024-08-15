package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.LoginUserRequest;
import com.shinhan.knockknock.domain.dto.LoginUserResponse;
import com.shinhan.knockknock.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody LoginUserRequest request) {
        LoginUserResponse response = null;
        try {
            if (request.getLoginType().equals("NORMAL")) {
                response = authService.loginUser(request);
            }
        } catch (Exception e) {
            response = LoginUserResponse.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(401).body(response);
        }
        return ResponseEntity.status(200).body(response);
    }
}
