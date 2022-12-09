package com.dmdev.domain.dto.accident;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Value
public class AccidentCreateRequestDto {

    @NotNull
    Long orderId;

    @NotNull
    LocalDate accidentDate;

    @NotBlank(message = "Description is mandatory")
    String description;

    @NotNull
    private BigDecimal damage;
}