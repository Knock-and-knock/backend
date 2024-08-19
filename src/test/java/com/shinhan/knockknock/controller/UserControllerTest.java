package com.shinhan.knockknock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.knockknock.domain.dto.CreateUserRequest;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.UserRole;
import com.shinhan.knockknock.repository.UserRepository;
import com.shinhan.knockknock.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    public void beforeTestSetting() {
        UserEntity testUser = UserEntity.builder()
                .userId("test01")
                .userPassword("1234")
                .userName("테스트")
                .userPhone("01012345678")
                .userType(UserRole.PROTEGE)
                .userSimplePassword("123456")
                .build();
        userRepository.save(testUser);
    }

    @DisplayName("아이디 중복체크 테스트")
    @Test
    public void duplicateCheckIdTest() throws Exception {
        String userId1 = "test03";
        String userId2 = "test02";

        Boolean result1 = userService.readUserId(userId1);
        Boolean result2 = userService.readUserId(userId2);

        assertThat(result1).isFalse();
        assertThat(result2).isTrue();

        String url1 = "/api/v1/users/validation/"+userId1;
        String url2 = "/api/v1/users/validation/"+userId2;

        mockMvc.perform(get(url1))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"이미 사용중인 아이디입니다.\", \"result\":false}"));

        mockMvc.perform(get(url2))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"사용가능한 아이디입니다.\", \"result\":true}"));
    }

    @DisplayName("회원가입 테스트")
    @Test
    public void testUserSignupSuccess() throws Exception {
        // given
        CreateUserRequest request = CreateUserRequest.builder()
                .userId("test02")
                .userPassword("1234")
                .userName("테스트02")
                .userPhone("01012341234")
                .userType(UserRole.PROTEGE)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody1 = objectMapper.writeValueAsString(request);

        // when
        Mockito.when(userService.createUser(request)).thenReturn(true);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .content(requestBody1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("회원가입에 성공하였습니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("true"));
    }

    @DisplayName("회원가입 테스트")
    @Test
    public void testUserSignupFail() throws Exception {
        // given
        CreateUserRequest request = CreateUserRequest.builder()
                .userId("test01")
                .userPassword("1234")
                .userName("테스트02")
                .userPhone("01012345678")
                .userType(UserRole.PROTEGE)
                .userSimplePassword("123456")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody1 = objectMapper.writeValueAsString(request);

        // when
        Mockito.when(userService.createUser(request)).thenReturn(false);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .content(requestBody1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("회원가입에 실패하였습니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("false"));
    }
}
