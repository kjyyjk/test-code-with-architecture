package com.example.demo.user.service.port;

import lombok.RequiredArgsConstructor;

public interface MailSender {

    void send(String email, String title, String content);
}
