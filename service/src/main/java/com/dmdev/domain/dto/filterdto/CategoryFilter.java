package com.dmdev.domain.dto.filterdto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class CategoryFilter {

    BigDecimal price;
    String type;
}