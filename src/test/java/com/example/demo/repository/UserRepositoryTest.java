package com.example.demo.repository;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest(showSql = true)
@Sql("/sql/user-repository-test-data.sql")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

/*    @Test
    void User_Repository_가_제대로_연결되었다() {
        //given
        UserEntity user = new UserEntity();
        user.setEmail("rlawnsdud920@naver.com");
        user.setAddress("Seoul");
        user.setNickname("kjyyjk");
        user.setStatus(UserStatus.ACTIVE);
        user.setCertificationCode("aaaaa-aaaaaaaaa-aaaaaa");

        //when
        UserEntity result = userRepository.save(user);

        //then
        assertThat(result.getId()).isNotNull();
    }*/

    @Test
    void findByIdAndStatus_로_유저데이터를_찾아올수있따() {
        //given
        //when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1L, UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optional_empty를내려준다() {
        //given
        //when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1L, UserStatus.PENDING);

        //then
        assertThat(result.isEmpty()).isTrue();
    }


    @Test
    void findByEmailAndStatus_로_유저데이터를_찾아올수있따() {
        //given
        //when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("rlawnsdud920@naver.com", UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty를내려준다() {
        //given
        //when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("rlawnsdud920@naver.com", UserStatus.PENDING);

        //then
        assertThat(result.isEmpty()).isTrue();
    }
}