package com.example.demo.post.domain;

import com.example.demo.post.domain.dto.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    public void Post는_PostCreate로_만들_수_있다() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .writerId(11L)
                .content("hello world")
                .build();

        User writer = User.builder()
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .certificationCode("aaaa-aaaaaa-aaaaaa")
                .status(UserStatus.ACTIVE)
                .build();

        //when
        Post post = Post.from(writer, postCreate);

        //then
        assertThat(post.getContent()).isEqualTo("hello world");
        assertThat(post.getWriter().getEmail()).isEqualTo("rlawnsdud920@naver.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("kjyyjk");
        assertThat(post.getWriter().getAddress()).isEqualTo("seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaa-aaaaaa-aaaaaa");
    }

}