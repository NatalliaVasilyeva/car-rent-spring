package com.dmdev.domain.dto.accident;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class AccidentResponseDto {

    @NotNull
    Long id;
    Long orderNumber;
    LocalDate date;
    String brand;
    String model;
    String carNumber;
    String userFirstName;
    String userLastName;
    String description;
    BigDecimal damage;
}