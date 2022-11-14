package com.dmdev.domain.dto.user.request;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class LoginRequestDto {

    @NotEmpty
    @Email
    String email;

    @NotEmpty
    @Size(min = 8, message = "Password should have at least 8 characters")
    String password;
}