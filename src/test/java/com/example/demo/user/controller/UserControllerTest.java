package com.example.demo.user.controller;

import com.example.demo.common.controller.response.MyProfileResponse;
import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.dto.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest {

    @Test
    public void 사용자는_특정_유저의_정보를_개인정보는_소거된채_전달받을_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .certificationCode("aaaa-aaaaaa-aaaaaa")
                .status(UserStatus.ACTIVE)
                .build());

        //when
        ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1L);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("rlawnsdud920@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("kjyyjk");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void 사용자는_존재하지_않는_유저의_id로_api를호출할경우_404_응답을_받는다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .certificationCode("aaaa-aaaaaa-aaaaaa")
                .status(UserStatus.ACTIVE)
                .build());

        //when
        //then
        assertThatThrownBy(() -> testContainer.userController.getUserById(2L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void 사용자는_인증_코드로_계정을_활성화할_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .certificationCode("aaaa-aaaaaa-aaaaaa")
                .status(UserStatus.PENDING)
                .build());

        //when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1L, "aaaa-aaaaaa-aaaaaa");

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.valueOf(302));
        assertThat(testContainer.userRepository.getById(1L).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void 사용자는_인증코드가_일치하지_않을_경우_권한없음_에러를_내려준다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .certificationCode("aaaa-aaaaaa-aaaaaa")
                .status(UserStatus.ACTIVE)
                .build());

        //when
        //then
        assertThatThrownBy(() -> testContainer.userController.verifyEmail(1L, "aa"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    public void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고올_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(100L))
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .certificationCode("aaaa-aaaaaa-aaaaaa")
                .status(UserStatus.ACTIVE)
                .build());

        //when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("rlawnsdud920@naver.com");

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("rlawnsdud920@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("kjyyjk");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
        assertThat(result.getBody().getAddress()).isEqualTo("seoul");
    }

    @Test
    public void 사용자는_내_정보를_수정할_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .certificationCode("aaaa-aaaaaa-aaaaaa")
                .status(UserStatus.ACTIVE)
                .build());

        //when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo("rlawnsdud920@naver.com", UserUpdate.builder()
                .address("busan")
                .nickname("kjy")
                .build());

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("rlawnsdud920@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("kjy");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getAddress()).isEqualTo("busan");
    }
}