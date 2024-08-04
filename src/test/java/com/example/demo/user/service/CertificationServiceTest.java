package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import com.example.demo.user.service.CertificationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class CertificationServiceTest {

    @Test
    public void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다() {
        //given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);

        //when
        certificationService.send("rlawnsdud920@naver.com", 11, "aaaa-aaaaaa-aaaaaa");

        //then
        assertThat(fakeMailSender.email).isEqualTo("rlawnsdud920@naver.com");
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content).isEqualTo(
                "Please click the following link to certify your email address: http://localhost:8080/api/users/11/verify?certificationCode=aaaa-aaaaaa-aaaaaa");
    }


}