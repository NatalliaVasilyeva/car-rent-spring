package com.dmdev.mapper.user;

import com.dmdev.domain.dto.driverlicense.response.DriverLicenseResponseDto;
import com.dmdev.domain.dto.user.response.UserResponseDto;
import com.dmdev.domain.dto.userdetails.response.UserDetailsResponseDto;
import com.dmdev.domain.entity.User;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper implements Mapper<User, UserResponseDto> {

    @Override
    public UserResponseDto map(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .role(user.getRole())
                .userDetailsDto(UserDetailsResponseDto.builder()
                        .id(user.getUserDetails().getId())
                        .userId(user.getUserDetails().getUser().getId())
                        .name(user.getUserDetails().getName())
                        .surname(user.getUserDetails().getSurname())
                        .address(user.getUserDetails().getUserContact().getAddress())
                        .phone(user.getUserDetails().getUserContact().getPhone())
                        .birthday(user.getUserDetails().getBirthday())
                        .registrationAt(user.getUserDetails().getRegistrationDate())
                        .build())
                .role(user.getRole())
                .driverLicenseDto(DriverLicenseResponseDto.builder()
                        .id(user.getUserDetails().getDriverLicenses().iterator().next().getId())
                        .userId(user.getUserDetails().getDriverLicenses()
                                .stream()
                                .limit(1)
                                .findFirst()
                                .get()
                                .getId())
                        .driverLicenseNumber(user.getUserDetails().getDriverLicenses().iterator().next().getNumber())
                        .driverLicenseIssueDate(user.getUserDetails().getDriverLicenses().iterator().next().getIssueDate())
                        .driverLicenseExpiredDate(user.getUserDetails().getDriverLicenses().iterator().next().getExpiredDate())
                        .build())
                .build();
    }
}