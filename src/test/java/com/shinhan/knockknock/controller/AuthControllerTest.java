package com.shinhan.knockknock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.user.IdLoginUserRequest;
import com.shinhan.knockknock.domain.dto.user.TokenResponse;
import com.shinhan.knockknock.service.user.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("아이디/패스워드 로그인 성공 테스트")
    public void testLoginSuccess() throws Exception {
        // given
        IdLoginUserRequest request = IdLoginUserRequest.builder()
                .userId("protector01")
                .userPassword("1234")
                .build();

        TokenResponse response = authService.loginUser(request);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login/normal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userNo").value(response.getUserNo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userType").value(response.getUserType().toString()));
    }

    @Test
    @DisplayName("아이디/패스워드 로그인 실패 테스트 - 존재하지 않는 아이디")
    public void testLoginFailUserNotFound() throws Exception {
        // given
        IdLoginUserRequest request = IdLoginUserRequest.builder()
                .userId("protector04")
                .userPassword("1234")
                .build();

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            authService.loginUser(request);
        });

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login/normal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    @DisplayName("아이디/패스워드 로그인 실패 테스트 - 비밀번호 불일치")
    public void testLoginFailBadCredentials() throws Exception {
        // given
        IdLoginUserRequest request = IdLoginUserRequest.builder()
                .userId("protector01")
                .userPassword("123456")
                .build();

        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            authService.loginUser(request);
        });

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login/normal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    @WithMockUser(username = "protege02", password = "1234")
    @DisplayName("로그아웃 성공 테스트")
    public void testLogoutSuccess() throws Exception {
        // given
        String accessToken = login("protege02");
        long userNo = Long.parseLong(jwtProvider.getUserNo(accessToken));
        String authorizationHeader = "Bearer "+accessToken;

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/logout")
                        .header("Authorization", authorizationHeader))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("로그아웃 되었습니다."));
    }

    @Test
    @WithMockUser(username = "protege02", password = "1234")
    @DisplayName("로그아웃 실패 테스트")
    public void testLogoutFail() throws Exception {
        // given
        String accessToken = login("protege02");
        long userNo = Long.parseLong(jwtProvider.getUserNo(accessToken));
        String authorizationHeader = "Bearer "+accessToken;

        authService.logoutUser(userNo);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/logout")
                        .header("Authorization", authorizationHeader))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Refresh Token이 존재하지 않습니다."));
    }

    private String login(String userId) {
        IdLoginUserRequest request = IdLoginUserRequest.builder()
                .userId(userId)
                .userPassword("1234")
                .build();
        TokenResponse response = authService.loginUser(request);
        return response.getAccessToken();
    }
}
