package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올수있다() {
        //given
        String email = "rlawnsdud920@naver.com";
        
        //when
        UserEntity result = userService.getByEmail(email);

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
        UserEntity result = userService.getById(1L);

        //then
        assertThat(result.getNickname()).isEqualTo("kjyyjk");
    }

    @Test
    void getById는_PENDING_상태인_유저를_찾아올수없다() {
        //given
        //when
        //then
        assertThatThrownBy(() -> userService.getById(2L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}