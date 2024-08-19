package com.shinhan.knockknock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.knockknock.domain.dto.LoginUserRequest;
import com.shinhan.knockknock.domain.dto.TokenResponse;
import com.shinhan.knockknock.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    @DisplayName("아이디/패스워드 로그인 성공 테스트")
    public void testLoginSuccess() throws Exception {
        // given
        LoginUserRequest request = LoginUserRequest.builder()
                .userId("protector01")
                .userPassword("1234")
                .loginType("NORMAL")
                .build();

        TokenResponse response = authService.loginUser(request);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
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
        LoginUserRequest request = LoginUserRequest.builder()
                .userId("test01")
                .userPassword("1234")
                .loginType("NORMAL")
                .build();

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            authService.loginUser(request);
        });

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
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
        LoginUserRequest request = LoginUserRequest.builder()
                .userId("protector01")
                .userPassword("123456")
                .loginType("NORMAL")
                .build();

        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            authService.loginUser(request);
        });

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(exception.getMessage()));
    }
}
