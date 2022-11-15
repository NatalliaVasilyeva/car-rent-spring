package com.dmdev.domain.dto.userdetails.response;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Value
@Builder
public class UserDetailsResponseDto {

    @NotEmpty
    private Long id;
    private String name;
    private String surname;
    private String address;
    private String phone;
    private LocalDate birthday;
}