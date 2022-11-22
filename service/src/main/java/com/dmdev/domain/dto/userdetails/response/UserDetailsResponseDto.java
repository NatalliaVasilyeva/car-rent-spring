package com.dmdev.domain.dto.userdetails.response;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Value
@Builder
public class UserDetailsResponseDto {

    @NotEmpty
    Long id;

    @NotEmpty
    Long userId;

    String name;
    String surname;
    String address;
    String phone;
    LocalDate birthday;
    LocalDate registrationAt;
}