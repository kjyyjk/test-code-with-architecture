package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    UserService userService;
    @MockBean
    JavaMailSender javaMailSender;

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

        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        //when
        User result = userService.create(userCreate);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        // assertThat(result.getCertificationCode).isEqualTo("T.T");
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
//        assertThat(result.getLastLoginAt()).isEqualTo("TT"); //FIXME
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