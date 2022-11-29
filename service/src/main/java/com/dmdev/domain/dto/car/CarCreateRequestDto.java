package com.dmdev.domain.dto.car;

import com.dmdev.annotation.EnumNamePattern;
import com.dmdev.domain.model.Color;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class CarCreateRequestDto {

    @NotNull(message = "Brand id can't be null")
    Long brandId;

    @NotNull
    Long modelId;

    @NotNull
    Long categoryId;

    @EnumNamePattern(regexp = "RED|BLUE|WHITE|BLACK|GREEN|YELLOW")
    Color color;

    @NotNull
    Integer yearOfProduction;

    @NotBlank(message = "Car number is mandatory")
    String number;

    @NotBlank(message = "Vin number is mandatory")
    @Size(min = 10)
    String vin;

    @NotNull
    Boolean isRepaired;

    MultipartFile image;
}