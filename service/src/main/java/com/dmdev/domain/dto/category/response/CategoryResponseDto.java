package com.dmdev.domain.dto.category.response;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Value
@Builder
public class CategoryResponseDto {

    @NotEmpty
    Long id;
    String name;
    BigDecimal price;
}