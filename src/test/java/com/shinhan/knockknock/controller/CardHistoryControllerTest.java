package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.service.cardcategory.CardCategoryService;
import com.shinhan.knockknock.service.cardhistory.CardHistoryService;
import com.shinhan.knockknock.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ActiveProfiles("test")
@WebMvcTest(CardHistoryController.class)
class CardHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardHistoryService cardHistoryService;

    @MockBean
    private CardCategoryService cardCategoryService; // 추가된 부분: CardCategoryService 모킹

    @MockBean
    private CardRepository cardRepository;

    private CardEntity cardEntity;

    @BeforeEach
    void setUp() {
        cardEntity = CardEntity.builder()
                .cardId(1L)
                .cardBank("신한은행")
                .cardAccount("1234-1234-1234")
                .cardIsfamily(true)
                .build();
    }

    @Test
    @WithMockUser(username = "protege02", password = "1234")
    @DisplayName("가족 카드의 관련 사용자를 성공적으로 조회")
    void getFamilyCardUserName_Success() throws Exception {
        // given: 카드가 존재하고 관련 사용자가 있을 때
        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(cardEntity));
        when(cardHistoryService.findUserNameForFamilyCard(cardEntity)).thenReturn("홍길동");

        // when: 가족 카드 관련 사용자를 조회하는 요청을 보낼 때
        mockMvc.perform(get("/api/v1/card-history/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("홍길동"));

        // then: 상태 코드 200과 사용자 이름 "홍길동"이 반환된다.
    }

    @Test
    @WithMockUser(username = "protege02", password = "1234")
    @DisplayName("가족 카드의 관련 사용자가 존재하지 않을 경우 404 응답")
    void getFamilyCardUserName_NoSuchElement() throws Exception {
        // given: 카드가 존재하지만 관련 사용자가 없을 때
        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(cardEntity));
        when(cardHistoryService.findUserNameForFamilyCard(cardEntity)).thenThrow(new NoSuchElementException("관련 사용자를 찾을 수 없습니다."));

        // when: 가족 카드 관련 사용자를 조회하는 요청을 보낼 때
        mockMvc.perform(get("/api/v1/card-history/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("관련 사용자를 찾을 수 없습니다."));

        // then: 상태 코드 404와 관련 없는 사용자 메시지가 반환된다.
    }

    @Test
    @WithMockUser(username = "protege02", password = "1234")
    @DisplayName("카드가 존재하지 않을 경우 404 응답")
    void getFamilyCardUserName_CardNotFound() throws Exception {
        // given: 카드가 존재하지 않을 때
        when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when: 가족 카드 관련 사용자를 조회하는 요청을 보낼 때
        mockMvc.perform(get("/api/v1/card-history/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("해당 카드가 존재하지 않습니다."));

        // then: 상태 코드 404와 카드가 존재하지 않는다는 메시지가 반환된다.
    }

}
