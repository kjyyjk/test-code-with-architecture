package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.dto.PostCreate;
import com.example.demo.post.domain.dto.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;

class PostTest {

    private final TestClockHolder testClockHolder = new TestClockHolder(100L);

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
        Post post = Post.from(writer, postCreate, testClockHolder);

        //then
        assertThat(post.getContent()).isEqualTo("hello world");
        assertThat(post.getWriter().getEmail()).isEqualTo("rlawnsdud920@naver.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("kjyyjk");
        assertThat(post.getWriter().getAddress()).isEqualTo("seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaa-aaaaaa-aaaaaa");
        assertThat(post.getCreatedAt()).isEqualTo(100L);
    }
    
    @Test
    public void Post는_PostUpdate로_수정할_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("foobar")
                .build();

        User user = User.builder()
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .certificationCode("aaaa-aaaaaa-aaaaaa")
                .status(UserStatus.ACTIVE)
                .build();

        Post post = Post.builder()
                .id(11L)
                .writer(user)
                .createdAt(1678530673958L)
                .modifiedAt(1678530673958L)
                .content("helloworld")
                .build();

        //when
        post = post.update(postUpdate, testClockHolder);

        //then
        assertThat(post.getContent()).isEqualTo("foobar");
        assertThat(post.getModifiedAt()).isEqualTo(100L);
    }

}