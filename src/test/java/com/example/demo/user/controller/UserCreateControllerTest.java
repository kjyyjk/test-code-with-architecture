package com.example.demo.user.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUUIDHolder;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.dto.UserCreate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

class UserCreateControllerTest {

    @Test
    public void  사용자는_회원가입을_할_수_있고_사용자는_PENDING_상태이다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .uuidHolder(new TestUUIDHolder("aaaa-aaaaaa-aaaaaa"))
                .build();

        //when
        ResponseEntity<UserResponse> result = testContainer.userCreateController.createUser(UserCreate.builder()
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .build());

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(0L);
        assertThat(result.getBody().getEmail()).isEqualTo("rlawnsdud920@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("kjyyjk");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getBody().getLastLoginAt()).isNull();
        assertThat(testContainer.userRepository.getById(0L).getCertificationCode()).isEqualTo("aaaa-aaaaaa-aaaaaa");
    }
}