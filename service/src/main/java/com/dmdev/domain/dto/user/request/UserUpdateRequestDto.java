package com.dmdev.domain.dto.user.request;

import com.dmdev.domain.model.Role;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class UserUpdateRequestDto {

    @NotEmpty
    @Email
    String email;

    @NotEmpty
    @Size(min = 2, message = "Login should have at least 2 characters")
    String login;

    Role role;
}