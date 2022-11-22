package com.dmdev.mapper.user;

import com.dmdev.domain.dto.user.request.UserUpdateRequestDto;
import com.dmdev.domain.entity.User;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateMapper implements Mapper<UserUpdateRequestDto, User> {

    @Override
    public User map(UserUpdateRequestDto object) {
        return User.builder()
                .email(object.getEmail())
                .username(object.getUsername())
                .build();
    }

    @Override
    public User map(UserUpdateRequestDto requestDto, User existing) {
        merge(requestDto, existing);
        return existing;
    }

    private void merge(UserUpdateRequestDto requestDto, User existing) {
        existing.setUsername(requestDto.getUsername());
        existing.setEmail(requestDto.getEmail());
        existing.setRole(requestDto.getRole());
    }
}