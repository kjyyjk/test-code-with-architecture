package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUUIDHolder;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    @Test
    public void User는_UserCreate_객체로_생성할_수_있다() {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("busan")
                .build();

        //when
        User user = User.from(userCreate, new TestUUIDHolder("aaaaa-aaaaaaaaa-aaaaaa"));

        //then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("rlawnsdud920@naver.com");
        assertThat(user.getNickname()).isEqualTo("kjyyjk");
        assertThat(user.getAddress()).isEqualTo("busan");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaa-aaaaaaaaa-aaaaaa");
    }

    @Test
    public void User는_UserUpdate_객체로_데이터를_업데이트할_수_있다() {
        //given
        User user = User.builder()
                .id(11L)
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaa-aaaaaa-aaaaaa")
                .build();

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("kjy")
                .address("busan")
                .build();

        //when
        user = user.update(userUpdate);

        //then
        assertThat(user.getId()).isEqualTo(11L);
        assertThat(user.getEmail()).isEqualTo("rlawnsdud920@naver.com");
        assertThat(user.getNickname()).isEqualTo("kjy");
        assertThat(user.getAddress()).isEqualTo("busan");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaa-aaaaaa-aaaaaa");
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    public void User는_로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
        //given
        User user = User.builder()
                .id(11L)
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaa-aaaaaa-aaaaaa")
                .build();

        //when
        user = user.login(new TestClockHolder(200L));

        //then
        assertThat(user.getLastLoginAt()).isEqualTo(200L);
    }

    @Test
    public void User는_유효한_인증코드로_계정을_활성화할_수_있다() {
        //given
        User user = User.builder()
                .id(11L)
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaa-aaaaaa-aaaaaa")
                .build();

        //when
        user = user.certificate("aaaa-aaaaaa-aaaaaa");

        //then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void User는_잘못된_인증코드로_계정을_활성화하려면_에러를_던진다() {
        //given
        User user = User.builder()
                .id(11L)
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaa-aaaaaa-aaaaaa")
                .build();

        //when
        //then
        assertThatThrownBy(() -> user.certificate("aaaa-aaaaaa-bbbbbb"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}