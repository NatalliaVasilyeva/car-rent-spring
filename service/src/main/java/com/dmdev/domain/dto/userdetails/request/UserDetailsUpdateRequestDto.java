package com.dmdev.domain.dto.userdetails.request;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserDetailsUpdateRequestDto {

    String name;
    String surname;
    String address;
    String phone;
    LocalDate birthday;
}