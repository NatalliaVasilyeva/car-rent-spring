package com.dmdev.domain.dto.car;

import com.dmdev.domain.model.Color;
import com.dmdev.domain.model.EngineType;
import com.dmdev.domain.model.Transmission;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Value
@Builder
public class CarResponseDto {

    @NotEmpty
    long id;
    String brand;
    String model;
    Color color;
    Transmission transmission;
    EngineType engineType;
    Integer yearOfProduction;
    String number;
    String vin;
    Boolean repaired;
    String image;
    String category;
    BigDecimal price;
}