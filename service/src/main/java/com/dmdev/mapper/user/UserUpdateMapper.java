package com.dmdev.mapper.user;

import com.dmdev.domain.dto.user.request.UserUpdateRequestDto;
import com.dmdev.domain.entity.User;
import com.dmdev.mapper.UpdateMapper;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateMapper implements UpdateMapper<UserUpdateRequestDto, User> {

    @Override
    public User mapToEntity(UserUpdateRequestDto requestDto, User existing) {
        merge(requestDto, existing);
        return existing;
    }

    @Override
    public void merge(UserUpdateRequestDto requestDto, User existing) {
        existing.setUsername(requestDto.getUsername());
        existing.setEmail(requestDto.getEmail());
        existing.setRole(requestDto.getRole());
    }
}