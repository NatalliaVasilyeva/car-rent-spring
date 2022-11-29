package com.dmdev.domain.dto.model;

import com.dmdev.annotation.EnumNamePattern;
import com.dmdev.domain.model.EngineType;
import com.dmdev.domain.model.Transmission;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class ModelCreateRequestDto {

    @NotNull(message = "Brand id can't be null")
    Long brandId;

    @NotBlank(message = "Name is mandatory")
    String name;

    @EnumNamePattern(regexp = "MANUAL|AUTOMATIC|ROBOT")
    Transmission transmission;

    @EnumNamePattern(regexp = " FUEL|ELECTRIC|GAS|DIESEL")
    EngineType engineType;
}