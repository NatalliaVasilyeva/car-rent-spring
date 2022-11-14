package com.dmdev.mapper.user;

import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.entity.User;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserCreateMapper implements Mapper<UserCreateRequestDto, User> {

    @Override
    public User map(UserCreateRequestDto requestDto) {
        return User.builder()
                .login(requestDto.getLogin())
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .build();
    }
}