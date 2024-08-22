package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateUserRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("회원 저장 테스트")
    public void testUserSignup(){
        // given
        CreateUserRequest request = CreateUserRequest.builder()
                .userId("test01")
                .userPassword("1234")
                .userName("테스트01")
                .userPhone("01012121212")
                .userSimplePassword("123456")
                .build();

        // when
        boolean result = userService.createUser(request);

        // then
        assertThat(result).isTrue();
    }
}
