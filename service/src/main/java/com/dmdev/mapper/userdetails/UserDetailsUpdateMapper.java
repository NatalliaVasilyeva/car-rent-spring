package com.dmdev.mapper.userdetails;

import com.dmdev.domain.dto.userdetails.request.UserDetailsUpdateRequestDto;
import com.dmdev.domain.entity.UserContact;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.mapper.UpdateMapper;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsUpdateMapper implements UpdateMapper<UserDetailsUpdateRequestDto, UserDetails> {


    @Override
    public UserDetails mapToEntity(UserDetailsUpdateRequestDto requestDto, UserDetails existing) {
        merge(requestDto, existing);
        return existing;
    }

    @Override
    public void merge(UserDetailsUpdateRequestDto requestDto, UserDetails existing) {
        existing.setName(requestDto.getName());
        existing.setSurname(requestDto.getSurname());
        existing.setUserContact(UserContact.builder()
                .address(requestDto.getAddress())
                .phone(requestDto.getPhone())
                .build());
    }
}