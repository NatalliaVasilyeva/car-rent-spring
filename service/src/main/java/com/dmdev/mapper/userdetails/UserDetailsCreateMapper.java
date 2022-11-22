package com.dmdev.mapper.userdetails;

import com.dmdev.domain.dto.userdetails.request.UserDetailsCreateRequestDto;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserContact;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsCreateMapper implements Mapper<UserDetailsCreateRequestDto, UserDetails> {

    @Override
    public UserDetails map(UserDetailsCreateRequestDto requestDto) {
        return UserDetails.builder()
                .user(User.builder()
                        .id(requestDto.getUserId())
                        .build())
                .name(requestDto.getName())
                .surname(requestDto.getSurname())
                .userContact(UserContact.builder()
                        .address(requestDto.getAddress())
                        .phone(requestDto.getPhone())
                        .build()
                )
                .birthday(requestDto.getBirthday())
                .build();
    }
}