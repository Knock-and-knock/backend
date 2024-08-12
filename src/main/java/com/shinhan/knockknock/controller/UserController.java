package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "User API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "아이디 중복확인", description = "회원가입시 아이디 중복확인을 위한 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 조회 성공")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<Boolean> duplicateCheckUserId(@PathVariable String userId) {
        Boolean result = userService.readUserId(userId);
        return ResponseEntity.ok(result);
    }
}