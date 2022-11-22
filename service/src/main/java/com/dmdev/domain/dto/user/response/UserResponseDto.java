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
    long id;
    String username;
    String email;
    Role role;
    UserDetailsResponseDto userDetailsDto;
    DriverLicenseResponseDto driverLicenseDto;
}