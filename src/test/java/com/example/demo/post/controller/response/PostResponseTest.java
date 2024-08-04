package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PostResponseTest {

    @Test
    public void Post로_응답을_생성할_수_있다() {
        //given
        User writer = User.builder()
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .certificationCode("aaaa-aaaaaa-aaaaaa")
                .status(UserStatus.ACTIVE)
                .build();

        Post post = Post.builder()
                .id(11L)
                .content("hello world")
                .writer(writer)
                .build();

        //when
        PostResponse postResponse = PostResponse.from(post);

        //then
        assertThat(postResponse.getContent()).isEqualTo("hello world");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("rlawnsdud920@naver.com");
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("kjyyjk");
        assertThat(postResponse.getWriter().getAddress()).isEqualTo("seoul");
        assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(postResponse.getWriter().getCertificationCode()).isEqualTo("aaaa-aaaaaa-aaaaaa");
    }
}