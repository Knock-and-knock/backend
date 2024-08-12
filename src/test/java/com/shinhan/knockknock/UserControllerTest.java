package com.shinhan.knockknock;

import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.UserRole;
import com.shinhan.knockknock.repository.UserRepository;
import com.shinhan.knockknock.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void beforeTestSetting() {
        Date today = new Date();
        long currentMilliseconds = today.getTime();
        java.sql.Date sqlDate = new java.sql.Date(currentMilliseconds);

        UserEntity testUser = UserEntity.builder()
                .userId("test01")
                .userPassword("1234")
                .userName("테스트")
                .userIsWithdraw(false)
                .userJoinDate(sqlDate)
                .userPhone("010-1234-5678")
                .userType(UserRole.PROTEGE)
                .build();
        userRepository.save(testUser);
    }

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @DisplayName("아이디 중복체크 테스트")
    @Test
    public void duplicateCheckIdTest() throws Exception {
        String userId1 = "test01";
        String userId2 = "test02";

        Boolean result1 = userService.readUserId(userId1);
        Boolean result2 = userService.readUserId(userId2);

        assertThat(result1).isTrue();
        assertThat(result2).isFalse();

        String url1 = "/api/users/"+userId1;
        String url2 = "/api/users/"+userId2;

        mockMvc.perform(get(url1))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        mockMvc.perform(get(url2))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
