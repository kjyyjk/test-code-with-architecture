package com.example.demo.mock;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {

    private final Long id = 0L;
    private final List<User> data = new ArrayList<>();

    @Override
    public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
        return data.stream().filter(item -> item.getId().equals(id) && item.getStatus().equals(userStatus)).findAny();
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
        return data.stream().filter(item -> item.getStatus().equals(userStatus) &&  item.getEmail().equals(email)).findAny();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId() == 0) {
            User newUser = User.builder()
                    .id(id)
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .address(user.getAddress())
                    .certificationCode(user.getCertificationCode())
                    .status(user.getStatus())
                    .lastLoginAt(user.getLastLoginAt())
                    .build();

            data.add(newUser);
            return newUser;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), user.getId()));
            data.add(user);
            return user;
        }
    }

    @Override
    public Optional<User> findById(long id) {
        return data.stream().filter(item -> item.getId().equals(id)).findAny();
    }
}
