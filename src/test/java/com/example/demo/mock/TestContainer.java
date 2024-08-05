package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UUIDHolder;
import com.example.demo.post.controller.PostController;
import com.example.demo.post.controller.PostCreateController;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {

    public final MailSender mailSender;
    public final UserRepository userRepository;
    public final PostRepository postRepository;
    public final PostService postService;
    public final CertificationService certificationServiceImpl;
    public final UserController userController;
    public final UserCreateController userCreateController;
    public final PostCreateController postCreateController;
    public final PostController postController;

    @Builder
    public TestContainer(ClockHolder clockHolder, UUIDHolder uuidHolder) {
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();
        this.postService = PostServiceImpl.builder()
                .userRepository(userRepository)
                .postRepository(postRepository)
                .clockHolder(clockHolder)
                .build();

        UserServiceImpl userService = UserServiceImpl.builder()
                .certificationServiceImpl(new CertificationService(mailSender))
                .userRepository(userRepository)
                .clockHolder(clockHolder)
                .uuidHolder(uuidHolder)
                .build();

        this.certificationServiceImpl = new CertificationService(mailSender);
        this.userController = UserController.builder()
                .userService(userService)
                .build();

        this.userCreateController = UserCreateController.builder()
                .userService(userService)
                .build();

        this.postController = PostController.builder()
                .postService(postService)
                .build();

        this.postCreateController = PostCreateController.builder()
                .postService(postService)
                .build();
    }
}
