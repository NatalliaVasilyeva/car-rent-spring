package com.dmdev.domain.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BrandFilter {
    String name;
}