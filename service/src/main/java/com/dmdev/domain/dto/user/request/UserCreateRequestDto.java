package com.dmdev.domain.dto.user.request;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
public class UserCreateRequestDto {

    @NotBlank(message = "Email is mandatory")
    @Email
    String email;

    @NotBlank(message = "Login is mandatory")
    @Size(min = 2, message = "Login should have at least 2 characters")
    String login;

    @NotEmpty
    @Size(min = 8, message = "Password should have at least 8 characters")
    @Pattern(regexp = "(?=^.{6,40}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "Password should have at least 8 characters")
    String password;

    @NotBlank(message = "Name is mandatory")
    String name;

    @NotBlank(message = "Surname is mandatory")
    String surname;

    @NotBlank(message = "Address is mandatory")
    String address;

    @NotBlank(message = "Phone is mandatory")
    @Pattern(regexp = "(\\+?(375|80)?\\s?)?\\(?(17|29|33|44|25)\\)?\\s?(\\d{3})[-|\\s]?(\\d{2})[-|\\s]?(\\d{2})")
    String phone;

    @NotNull
    LocalDate birthday;

    @NotBlank(message = "Driver license number is mandatory")
    @Size(min = 4, message = "Driver license number should have at least 2 characters")
    String driverLicenseNumber;

    @NotNull
    LocalDate driverLicenseIssueDate;

    @NotNull
    LocalDate driverLicenseExpiredDate;
}