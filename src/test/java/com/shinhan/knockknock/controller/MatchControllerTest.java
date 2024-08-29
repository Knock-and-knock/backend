package com.shinhan.knockknock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.knockknock.domain.dto.match.CreateMatchRequest;
import com.shinhan.knockknock.domain.dto.match.CreateMatchResponse;
import com.shinhan.knockknock.domain.dto.match.UpdateMatchRequest;
import com.shinhan.knockknock.domain.entity.MatchEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.UserRoleEnum;
import com.shinhan.knockknock.repository.MatchRepository;
import com.shinhan.knockknock.repository.UserRepository;
import com.shinhan.knockknock.service.match.MatchService;
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

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MatchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MatchService matchService;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "protector01", password = "1234")
    @DisplayName("매칭 조회 성공 테스트")
    public void testReadMatchSuccess() throws Exception {
        // given
        CreateMatchResponse response = matchService.readMatch();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/match"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchProtectorName")
                        .value(response.getMatchProtectorName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchProtegeName")
                        .value(response.getMatchProtegeName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchStatus").value(response.getMatchStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("조회 성공"));
    }

    @Test
    @WithMockUser(username = "test", password = "1234")
    @DisplayName("매칭 조회 실패 테스트")
    public void testReadMatchFail() throws Exception {
        // given
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            matchService.readMatch();
        });

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/match"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("매칭 정보가 없습니다."));
    }

    @Test
    @WithMockUser(username = "protector01", password = "1234")
    @DisplayName("매칭 요청 성공 테스트")
    public void testCreateMatchSuccess() throws Exception {
        // given
        CreateMatchRequest request = CreateMatchRequest.builder()
                .protegeName("피보호자01")
                .protegePhone("01056781234")
                .matchProtectorName("보호자")
                .matchProtegeName("피보호자")
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/match")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchProtectorName")
                        .value(request.getMatchProtectorName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchProtegeName")
                        .value(request.getMatchProtegeName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.protectorUserNo")
                        .isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.protegeUserNo")
                        .isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("매칭 요청 성공"));
    }

    @Test
    @WithMockUser(username = "protector01", password = "1234")
    @DisplayName("매칭 요청 실패 테스트")
    public void testCreateMatchFail() throws Exception {
        // given
        CreateMatchRequest request = CreateMatchRequest.builder()
                .protegeName("피보호자")
                .protegePhone("01056781234")
                .matchProtectorName("보호자")
                .matchProtegeName("피보호자")
                .build();
        String requestBody = objectMapper.writeValueAsString(request);


        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/match")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("회원이 존재하지 않습니다."));
    }

    @Test
    @WithMockUser(username = "protege01", password = "1234")
    @DisplayName("매칭 응답 성공 테스트")
    public void testUpdateMatchSuccess() throws Exception {
        // given
        UserEntity user = userRepository.findByUserId("protege01")
                .orElse(null);
        MatchEntity match = matchRepository.findByUserProtectorOrUserProtege(user, user)
                .orElse(new MatchEntity());
        UpdateMatchRequest request = UpdateMatchRequest.builder()
                .matchNo(match.getMatchNo())
                .matchStatus("ACCEPT")
                .build();
        String requestBody = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/match")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("응답 성공"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchStatus").value("ACCEPT"));
    }

    @Test
    @WithMockUser(username = "protector03", password = "1234")
    @DisplayName("매칭 응답 실패 테스트 - 피보호자가 아닌 경우")
    public void testUpdateMatchFailNotProtege() throws Exception {
        // given
        MatchEntity match = matchSetting("WAIT");
        UpdateMatchRequest request = UpdateMatchRequest.builder()
                .matchNo(match.getMatchNo())
                .matchStatus("ACCEPT")
                .build();
        String requestBody = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/match")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 접근입니다."));
    }

    @Test
    @WithMockUser(username = "protege03", password = "1234")
    @DisplayName("매칭 응답 실패 테스트 - 이미 수락된 경우")
    public void testUpdateMatchFailAlreadyAccept() throws Exception {
        // given
        MatchEntity match = matchSetting("ACCEPT");

        UpdateMatchRequest request = UpdateMatchRequest.builder()
                .matchNo(match.getMatchNo())
                .matchStatus("ACCEPT")
                .build();
        String requestBody = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/match")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("이미 수락한 요청입니다."));
    }

    @Test
    @WithMockUser(username = "protege03", password = "1234")
    @DisplayName("매칭 중단 성공 테스트")
    public void testDeleteMatchSuccess() throws Exception {
        // given
        MatchEntity match = matchSetting("ACCEPT");
        long matchNo = match.getMatchNo();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/match/{matchNo}", matchNo))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("매칭이 중단되었습니다."));
    }

    @Test
    @WithMockUser(username = "protege03", password = "1234")
    @DisplayName("매칭 중단 실패 테스트 - 존재하지 않는 매칭")
    public void testDeleteMatchFailNotMatch() throws Exception {
        // given
        MatchEntity match = matchSetting("ACCEPT");
        long matchNo = 999L; // 존재하지 않는 매칭 번호

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/match/{matchNo}", matchNo))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("매칭이 존재하지 않습니다."));
    }

    @Test
    @WithMockUser(username = "protege03", password = "1234")
    @DisplayName("매칭 중단 실패 테스트 - 잘못된 매칭 상태")
    public void testDeleteMatch() throws Exception {
        // given
        MatchEntity match = matchSetting("REJECT");
        long matchNo = match.getMatchNo();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/match/{matchNo}", matchNo))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 접근입니다."));
    }

    public MatchEntity matchSetting(String matchStatus) {
        UserEntity protectorUser = userRepository.save(
                UserEntity.builder()
                        .userId("protector03")
                        .userPassword("1234")
                        .userType(UserRoleEnum.PROTECTOR)
                        .build());

        UserEntity protegeUser = userRepository.save(
                UserEntity.builder()
                        .userId("protege03")
                        .userPassword("1234")
                        .userType(UserRoleEnum.PROTEGE)
                        .build());

        MatchEntity match = matchRepository.save(
                MatchEntity.builder()
                        .matchProtectorName("보호자")
                        .matchProtegeName("피보호자")
                        .userProtector(protectorUser)
                        .userProtege(protegeUser)
                        .matchStatus(matchStatus)
                        .build());

        return match;
    }
}
