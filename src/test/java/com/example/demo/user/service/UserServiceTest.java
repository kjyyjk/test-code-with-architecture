package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUUIDHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    private UserServiceImpl userService;

    @BeforeEach
    public void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();

        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        userService = UserServiceImpl.builder()
                .certificationService(new CertificationServiceImpl(fakeMailSender))
                .userRepository(fakeUserRepository)
                .clockHolder(new TestClockHolder(100L))
                .uuidHolder(new TestUUIDHolder("aaaaaa"))
                .build();

        fakeUserRepository.save(User.builder()
                        .id(11L)
                        .email("rlawnsdud920@naver.com")
                        .nickname("kjyyjk")
                        .address("seoul")
                        .certificationCode("aaaa-aaaaaa-aaaaaa")
                        .status(UserStatus.ACTIVE)
                        .lastLoginAt(0L).build());

        fakeUserRepository.save(User.builder()
                        .id(12L)
                        .email("rlawnsdud921@naver.com")
                        .nickname("kjyyjk")
                        .address("seoul")
                        .certificationCode("aaaa-aaaaaa-aaaaab")
                        .status(UserStatus.PENDING)
                        .lastLoginAt(0L).build());
    }

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올수있다() {
        //given
        String email = "rlawnsdud920@naver.com";
        
        //when
        User result = userService.getByEmail(email);

        //then
        assertThat(result.getNickname()).isEqualTo("kjyyjk");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올수없다() {
        //given
        String email = "rlawnsdud921@naver.com";

        //when
        //then
        assertThatThrownBy(() -> userService.getByEmail(email))
                .isInstanceOf(ResourceNotFoundException.class);
    }
    
    @Test
    void getById는_ACTIVE_상태인_유저를_찾아올수있다() {
        //given
        //when
        User result = userService.getById(11L);

        //then
        assertThat(result.getNickname()).isEqualTo("kjyyjk");
    }

    @Test
    void getById는_PENDING_상태인_유저를_찾아올수없다() {
        //given
        //when
        //then
        assertThatThrownBy(() -> userService.getById(12L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto를_이용하여_유저를_생성할_수_있다() {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("rlawnsdud922@naver.com")
                .address("Gyeonggi")
                .nickname("kjyyjk")
                .build();

        //when
        User result = userService.create(userCreate);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaaaaa");
    }

    @Test
    void userUpdateDto를_이용하여_유저를_수정할_수_있다() {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("america")
                .nickname("kjyyjkkjyyjk")
                .build();

        //when
        userService.update(11L, userUpdate);

        //then
        User result = userService.getById(11L);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getAddress()).isEqualTo("america");
        assertThat(result.getNickname()).isEqualTo("kjyyjkkjyyjk");
    }

    @Test
    void user를_로그인시키면_마지막_로그인_시간이_변경된다() {
        //given
        //when
        userService.login(11L);

        //then
        User result = userService.getById(11L);
        assertThat(result.getLastLoginAt()).isGreaterThan(0L);
        assertThat(result.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    void PENDING_상태의_사용자는_인증코드로_ACTIVE_시킬수있다() {
        //given
        //when
        userService.verifyEmail(11L, "aaaa-aaaaaa-aaaaaa");

        //then
        User result = userService.getById(11L);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증코드를_받으면_에러를_던진다() {
        //given
        //when
        //then
        assertThatThrownBy(() -> userService.verifyEmail(11L, "aaaa-aaaaaa-aaaaab"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}