package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserResponseTest {

    @Test
    public void User로_응답을_생성할_수_있다() {
        //given
        User user = User.builder()
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .certificationCode("aaaa-aaaaaa-aaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .build();

        //when
        UserResponse userResponse = UserResponse.from(user);

        //then
        assertThat(userResponse.getEmail()).isEqualTo("rlawnsdud920@naver.com");
        assertThat(userResponse.getNickname()).isEqualTo("kjyyjk");
        assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);
    }
}