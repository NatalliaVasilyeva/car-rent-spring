package com.dmdev.domain.dto.brand.request;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class BrandCreateEditRequestDto {

    @NotBlank(message = "Brand name is mandatory")
    String name;
}