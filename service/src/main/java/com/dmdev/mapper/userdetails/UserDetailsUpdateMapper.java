package com.dmdev.mapper.userdetails;

import com.dmdev.domain.dto.userdetails.request.UserDetailsUpdateRequestDto;
import com.dmdev.domain.entity.UserContact;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsUpdateMapper implements Mapper<UserDetailsUpdateRequestDto, UserDetails> {

    @Override
    public UserDetails map(UserDetailsUpdateRequestDto object) {
        return UserDetails.builder()
                .name(object.getName())
                .surname(object.getSurname())
                .userContact(UserContact.builder()
                        .address(object.getAddress())
                        .phone(object.getPhone())
                        .build())
                .build();
    }

    @Override
    public UserDetails map(UserDetailsUpdateRequestDto requestDto, UserDetails existing) {
        merge(requestDto, existing);
        return existing;
    }

    private void merge(UserDetailsUpdateRequestDto requestDto, UserDetails existing) {
        existing.setName(requestDto.getName());
        existing.setSurname(requestDto.getSurname());
        existing.setUserContact(UserContact.builder()
                .address(requestDto.getAddress())
                .phone(requestDto.getPhone())
                .build());
    }
}