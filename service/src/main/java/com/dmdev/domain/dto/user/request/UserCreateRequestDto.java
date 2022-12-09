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

    @NotBlank(message = "{email.notempty}")
    @Email
    String email;

    @NotBlank(message = "{username.notempty}")
    @Size(min = 2, message = "{username.size}")
    String username;

    @NotEmpty(message = "{password.notempty}")
    @Size(min = 8, message = "{password.size}")
    @Pattern(regexp = "(?=^.{6,40}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "{password.pattern}")
    String password;

    @NotBlank(message = "{name.notempty}")
    String name;

    @NotBlank(message = "{surname.notempty}")
    String surname;

    @NotBlank(message = "{address.notempty}")
    String address;

    @NotBlank(message = "{phone.notempty}")
    @Pattern(regexp = "(\\+?(375|80)?\\s?)?\\(?(17|29|33|44|25)\\)?\\s?(\\d{3})[-|\\s]?(\\d{2})[-|\\s]?(\\d{2})", message = "{phone.pattern}")
    String phone;

    @NotNull(message = "{birthday.notempty}")
    LocalDate birthday;

    @NotBlank(message = "{licensenumber.notempty}")
    @Size(min = 4, message = "{licensenumber.size}")
    String driverLicenseNumber;

    @NotNull(message = "{licenseisseuedate.notempty}")
    LocalDate driverLicenseIssueDate;

    @NotNull(message = "{licenseexpireredate.notempty}")
    LocalDate driverLicenseExpiredDate;
}