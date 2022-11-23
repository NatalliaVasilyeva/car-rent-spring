package com.dmdev.mapper.userdetails;

import com.dmdev.domain.dto.userdetails.response.UserDetailsResponseDto;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsResponseMapper implements ResponseMapper<UserDetails, UserDetailsResponseDto> {

    @Override
    public UserDetailsResponseDto mapToDto(UserDetails userDetails) {
        return UserDetailsResponseDto.builder()
                .id(userDetails.getId())
                .userId(userDetails.getUser().getId())
                .name(userDetails.getName())
                .surname(userDetails.getSurname())
                .address(userDetails.getUserContact().getAddress())
                .phone(userDetails.getUserContact().getPhone())
                .birthday(userDetails.getBirthday())
                .registrationAt(userDetails.getRegistrationDate())
                .build();
    }
}