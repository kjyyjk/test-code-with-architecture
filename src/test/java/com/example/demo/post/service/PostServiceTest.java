package com.example.demo.post.service;

import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.dto.PostCreate;
import com.example.demo.post.domain.dto.PostUpdate;
import com.example.demo.post.infrastructure.PostEntity;
import com.example.demo.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class PostServiceTest {

    @Autowired
    PostService postService;

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
        assertThat(result.getCreatedAt()).isGreaterThan(0);
        // assertThat(result.getCertificationCode).isEqualTo("T.T");
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
        assertThat(result.getModifiedAt()).isGreaterThan(0);
    }
}