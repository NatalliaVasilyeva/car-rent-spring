package com.dmdev.domain.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserDetailsFilter {
    String name;
    String surname;
    LocalDate birthday;
    String phone;
    String address;
}