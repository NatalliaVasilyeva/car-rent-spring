package com.dmdev.domain.dto.user.response;

import com.dmdev.domain.dto.driverlicense.response.DriverLicenseResponseDto;
import com.dmdev.domain.dto.userdetails.response.UserDetailsResponseDto;
import com.dmdev.domain.model.Role;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Value
@Builder
public class UserResponseDto {

    @NotEmpty
    private long id;
    private String login;
    private String email;
    private Role role;
    private UserDetailsResponseDto userDetailsDto;
    private DriverLicenseResponseDto driverLicenseDto;
}