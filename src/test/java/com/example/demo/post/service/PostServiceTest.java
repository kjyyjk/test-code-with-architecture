package com.example.demo.post.service;

import com.example.demo.mock.*;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.dto.PostCreate;
import com.example.demo.post.domain.dto.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostServiceTest {

    private PostServiceImpl postService;

    @BeforeEach
    public void init() {
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakePostRepository fakePostRepository = new FakePostRepository();

        postService = PostServiceImpl.builder()
                .userRepository(fakeUserRepository)
                .postRepository(fakePostRepository)
                .clockHolder(new TestClockHolder(100L))
                .build();

        User user = User.builder()
                .id(11L)
                .email("rlawnsdud920@naver.com")
                .nickname("kjyyjk")
                .address("seoul")
                .certificationCode("aaaa-aaaaaa-aaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L).build();

        fakePostRepository.save(Post.builder()
                .id(11L)
                .writer(user)
                .createdAt(1678530673958L)
                .modifiedAt(1678530673958L)
                .content("helloworld")
                .build());

        fakeUserRepository.save(user);

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
    void getById는_존재하는_게시물을_내려준다() {
        //given
        //when
        Post result = postService.getById(11L);

        //then
        assertThat(result.getContent()).isEqualTo("helloworld");
        assertThat(result.getWriter().getEmail()).isEqualTo("rlawnsdud920@naver.com");
    }

    @Test
    void postCreateDto를_이용하여_게시물을_생성할_수_있다() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .writerId(11L)
                .content("foobar")
                .build();

        //when
        Post result = postService.create(postCreate);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("foobar");
        assertThat(result.getCreatedAt()).isEqualTo(100L);
    }

    @Test
    void postUpdateDto를_이용하여_게시물을_수정할_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("hello world :)")
                .build();

        //when
        postService.update(11L, postUpdate);

        //then
        Post result = postService.getById(11L);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("hello world :)");
        assertThat(result.getModifiedAt()).isEqualTo(100L);
    }
}