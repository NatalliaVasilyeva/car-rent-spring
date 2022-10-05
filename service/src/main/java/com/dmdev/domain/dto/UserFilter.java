package com.dmdev.domain.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserFilter {
    String login;
    String email;
    String password;
    String name;
    String surname;
    LocalDate birthday;
    LocalDate expiredLicenseDriverDate;
}