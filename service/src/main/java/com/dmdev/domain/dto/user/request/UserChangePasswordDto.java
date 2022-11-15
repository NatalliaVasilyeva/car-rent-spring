package com.dmdev.domain.dto.user.request;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class UserChangePasswordDto {

    @NotEmpty
    @Size(min = 8, message = "Password should have at least 8 characters")
    String oldPassword;

    @NotEmpty
    @Size(min = 8, message = "Password should have at least 8 characters")
    String newPassword;
}