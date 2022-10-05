package com.dmdev.domain.dto;

import com.dmdev.domain.model.EngineType;
import com.dmdev.domain.model.Transmission;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ModelFilter {
    String brandName;
    String categoryName;
    String name;
    Transmission transmission;
    EngineType engineType;
}