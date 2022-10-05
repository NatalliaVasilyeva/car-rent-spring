package com.dmdev.domain.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class CategoryFilter {
    String name;
    BigDecimal price;
}