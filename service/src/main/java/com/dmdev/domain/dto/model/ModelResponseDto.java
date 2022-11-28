package com.dmdev.domain.dto.model;

import com.dmdev.domain.model.EngineType;
import com.dmdev.domain.model.Transmission;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Value
@Builder
public class ModelResponseDto {

    @NotEmpty
    long id;

    String brand;
    String name;
    Transmission transmission;
    EngineType engineType;
}