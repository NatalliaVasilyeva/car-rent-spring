package com.dmdev.domain.dto.user.request;

import com.dmdev.annotation.EnumNamePattern;
import com.dmdev.domain.model.Role;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class UserUpdateRequestDto {

    @NotBlank(message = "Email is mandatory")
    @Email
    String email;

    @NotBlank(message = "Username is mandatory")
    @Size(min = 2, message = "Username should have at least 2 characters")
    String username;

    @EnumNamePattern(regexp = "CLIENT|ADMIN")
    Role role;
}