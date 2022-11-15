package com.dmdev.domain.dto.user.request;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
public class UserCreateRequestDto {

    @NotEmpty
    @Email
    String email;

    @NotEmpty
    @Size(min = 2, message = "Login should have at least 2 characters")
    String login;

    @NotEmpty
    @Size(min = 8, message = "Password should have at least 8 characters")
    String password;

    @NotEmpty
    String name;

    @NotEmpty
    String surname;

    @NotEmpty
    String address;

    @NotEmpty
    String phone;

    @NotEmpty
    LocalDate birthday;

    @NotBlank(message = "Driver license number is mandatory")
    @Size(min = 4, message = "Driver license number should have at least 2 characters")
    String driverLicenseNumber;

    @NotEmpty
    LocalDate driverLicenseIssueDate;

    @NotEmpty
    LocalDate driverLicenseExpiredDate;
}