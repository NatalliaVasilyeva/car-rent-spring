package com.dmdev.mapper.user;

import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.entity.UserContact;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsFromUserCreateMapper implements Mapper<UserCreateRequestDto, UserDetails> {

    @Override
    public UserDetails map(UserCreateRequestDto requestDto) {
        return UserDetails.builder()
                .name(requestDto.getName())
                .surname(requestDto.getSurname())
                .userContact(UserContact.builder()
                        .address(requestDto.getAddress())
                        .phone(requestDto.getPhone())
                        .build())
                .birthday(requestDto.getBirthday())
                .build();
    }
}