package com.dmdev.mapper.user;

import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.domain.entity.UserContact;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.mapper.CreateMapper;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsFromUserCreateMapper implements CreateMapper<UserCreateRequestDto, UserDetails> {

    @Override
    public UserDetails mapToEntity(UserCreateRequestDto requestDto) {
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