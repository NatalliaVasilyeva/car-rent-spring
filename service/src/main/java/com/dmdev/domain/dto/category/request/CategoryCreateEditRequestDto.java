package com.dmdev.domain.dto.category.request;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
public class CategoryCreateEditRequestDto {

    @NotBlank(message = "Brand name is mandatory")
    String name;

    @NotNull(message = "Price is mandatory")
    BigDecimal price;
}