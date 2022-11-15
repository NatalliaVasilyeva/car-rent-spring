package com.dmdev.domain.dto.userdetails.request;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
@Builder
public class UserDetailsUpdateRequestDto {

    @NotEmpty
    String name;

    @NotEmpty
    String surname;

    @NotEmpty
    String address;

    @NotEmpty
    String phone;

    @NotNull
    LocalDate birthday;
}