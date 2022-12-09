package com.dmdev.domain.dto.accident;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
public class AccidentUpdateRequestDto {

    @NotBlank(message = "Description is mandatory")
    String description;

    @NotNull
    private BigDecimal damage;
}