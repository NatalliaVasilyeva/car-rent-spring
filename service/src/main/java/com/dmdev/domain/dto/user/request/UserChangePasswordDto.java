package com.dmdev.domain.dto.user.request;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Value
public class UserChangePasswordDto {

    @NotEmpty
    @Size(min = 6, message = "Password should have at least 8 characters")
    @Pattern(regexp = "(?=^.{6,40}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "Password should have at least 8 characters")
    String oldPassword;

    @NotEmpty
    @Size(min = 8, message = "Password should have at least 8 characters")
    @Pattern(regexp = "(?=^.{6,40}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "Password should have at least 8 characters")
    String newPassword;
}