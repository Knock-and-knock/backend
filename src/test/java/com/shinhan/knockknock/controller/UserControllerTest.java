package com.shinhan.knockknock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.user.*;
import com.shinhan.knockknock.domain.entity.UserRoleEnum;
import com.shinhan.knockknock.repository.UserRepository;
import com.shinhan.knockknock.service.user.AuthService;
import com.shinhan.knockknock.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private AuthService authService;

    /*@BeforeEach
    public void beforeTestSetting() {
        UserEntity testUser = UserEntity.builder()
                .userId("test01")
                .userPassword("1234")
                .userName("테스트")
                .userPhone("01012345678")
                .userType(UserRoleEnum.PROTEGE)
                .userSimplePassword("123456")
                .build();
        userRepository.save(testUser);
    }*/

    @DisplayName("아이디 중복체크 테스트")
    @Test
    public void duplicateCheckIdTest() throws Exception {
        // given
        String userId1 = "protector01";
        String userId2 = "test02";

        boolean result1 = userService.readUserId(userId1);
        boolean result2 = userService.readUserId(userId2);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/validation/" + userId1))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("이미 사용중인 아이디입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(result1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/validation/" + userId2))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("사용가능한 아이디입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(result2));
    }

    @DisplayName("회원가입 성공 테스트")
    @Test
    public void testUserSignupSuccess() throws Exception {
        // given
        CreateUserRequest request = CreateUserRequest.builder()
                .userId("test02")
                .userPassword("1234")
                .userName("테스트02")
                .userPhone("01056785678")
                .userType(UserRoleEnum.PROTEGE)
                .isBioLogin(true)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody1 = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/signup")
                        .content(requestBody1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("회원가입에 성공하였습니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("true"));
    }

    @DisplayName("회원가입 실패 테스트")
    @Test
    public void testUserSignupFail() throws Exception {
        // given
        CreateUserRequest request = CreateUserRequest.builder()
                .userId("test")
                .userPassword("1234")
                .userName("테스트")
                .userPhone("01087654321")
                .userType(UserRoleEnum.PROTEGE)
                .userSimplePassword("123456")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody1 = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/signup")
                        .content(requestBody1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(409))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("이미 존재하는 아이디입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("false"));
    }

    @DisplayName("회원 조회 성공 테스트")
    @Test
    @WithMockUser(username = "protector01", password = "1234")
    public void testReadUserSuccess() throws Exception {
        // given
        String accessToken = login("protector01");
        System.out.println(accessToken);
        long userNo = Long.parseLong(jwtProvider.getUserNo(accessToken));
        System.out.println(userNo);
        String authorizationHeader = "Bearer "+accessToken;
        ReadUserResponse response = userService.readUser(userNo);

        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = objectMapper.writeValueAsString(response);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .header("Authorization", authorizationHeader))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseJson));
    }

    @DisplayName("회원 탈퇴 성공 테스트")
    @Test
    @WithMockUser(username = "test", password = "1234")
    public void testDeleteUserSuccess() throws Exception {
        // given
        String accessToken = login("test");
        System.out.println("accessToken: "+accessToken);
        long userNo = Long.parseLong(jwtProvider.getUserNo(accessToken));
        String authorizationHeader = "Bearer "+accessToken;

        //Boolean result = userService.deleteUser(userNo);
        UserValidationResponse response = UserValidationResponse.builder()
                .message("탈퇴가 완료되었습니다.")
                .result(true)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = objectMapper.writeValueAsString(response);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/withdraw")
                        .header("Authorization", authorizationHeader))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseJson));
    }

    @DisplayName("회원 정보 수정 성공 테스트")
    @Test
    @WithMockUser(username = "protector01", password = "1234")
    public void testUpdateUserSuccess() throws Exception {
        // given
        String accessToken = login("protege01");
        long userNo = Long.parseLong(jwtProvider.getUserNo(accessToken));
        String authorizationHeader = "Bearer "+accessToken;

        UpdateUserRequest request = UpdateUserRequest.builder()
                .userGender(1)
                .userHeight(168)
                .userWeight(66)
                .userDisease("당뇨, 고혈압")
                .userAddress("경기도 성남시 분당구 정자일로 95")
                .build();
        ReadUserResponse response = userService.updateUser(userNo, request);

        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = objectMapper.writeValueAsString(response);
        String requestBody = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users")
                        .header("Authorization", authorizationHeader)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseJson));
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
