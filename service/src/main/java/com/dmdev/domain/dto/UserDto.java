package com.dmdev.domain.dto;

import lombok.Value;

import java.time.LocalDate;

@Value
public class UserDto {
    String email;
    String name;
    String surname;
    LocalDate birthday;
    String phone;
    String address;
}