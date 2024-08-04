package com.example.demo.common.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MyProfileResponseTest {

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
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        //then
        assertThat(myProfileResponse.getEmail()).isEqualTo("rlawnsdud920@naver.com");
        assertThat(myProfileResponse.getNickname()).isEqualTo("kjyyjk");
        assertThat(myProfileResponse.getAddress()).isEqualTo("seoul");
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);
    }
}