package com.dmdev.domain.dto.userdetails.request;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Value
public class UserDetailsUpdateRequestDto {

    @NotEmpty
    String name;

    @NotEmpty
    String surname;

    @NotEmpty
    String address;

    @NotEmpty
    @Pattern(regexp = "(\\+?(375|80)?\\s?)?\\(?(17|29|33|44|25)\\)?\\s?(\\d{3})[-|\\s]?(\\d{2})[-|\\s]?(\\d{2})")
    String phone;
}