package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.cardhistory.CreateCardHistoryRequest;
import com.shinhan.knockknock.domain.dto.user.IdLoginUserRequest;
import com.shinhan.knockknock.domain.dto.user.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;

@Controller
@RequiredArgsConstructor
@RequestMapping("/test")
@Slf4j
public class CardHistoryDataController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtProvider jwtProvider;

    // 로그인 페이지로 연결
    @GetMapping("/login-page")
    public String showLoginPage() {
        return "redirect:/login.html"; // login.html 파일이 loginPage로 렌더링됨
    }

    // 로그인 요청 처리
    @PostMapping("/login-page")
    public String handleLogin(IdLoginUserRequest request, Model model) {
        String apiUrl = "http://localhost:9090/api/v1/auth/login/normal";

        try {
            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(apiUrl, request, TokenResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("token", response.getBody().getAccessToken());
                return "redirect:/card-history";  // 로그인 성공 시 리다이렉트
            } else {
                model.addAttribute("message", "로그인 실패: " + response.getBody().getMessage());
                return "redirect:/login.html";  // 실패 시 loginPage로 리턴
            }
        } catch (Exception e) {
            model.addAttribute("message", "로그인 처리 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/login.html";  // 예외 발생 시에도 loginPage로 리턴
        }
    }

    // 카드 내역 입력 페이지로 연결
    @GetMapping("/card-history")
    public String showCardHistoryForm(Model model) {
        return "redirect:/card-history.html"; // createCardHistory.html 페이지로 연결
    }

    @PostMapping("/card-history")
    public String submitForm(CreateCardHistoryRequest request, @RequestHeader("Authorization") String header) {
        String apiUrl = "http://localhost:9090/api/v1/card-history"; // 실제 API URL
        Long token = jwtProvider.getUserNoFromHeader(header);


            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<CreateCardHistoryRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Long> response = restTemplate.postForEntity(apiUrl, entity, Long.class);

        return "redirect:/card-history.html"; // 결과 메시지를 다시 폼 페이지에 표시
    }
}
