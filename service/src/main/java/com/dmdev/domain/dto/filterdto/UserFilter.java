package com.dmdev.domain.dto.filterdto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserFilter {

    String username;
    String email;
    String name;
    String surname;
    LocalDate birthday;
    LocalDate expiredLicenseDriverDate;
    Boolean allExpiredLicenses;
}