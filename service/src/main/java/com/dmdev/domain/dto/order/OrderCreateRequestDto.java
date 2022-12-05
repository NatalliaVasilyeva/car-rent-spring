package com.dmdev.domain.dto.order;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class OrderCreateRequestDto {

    @NotNull
    Long userId;

    @NotNull
    Long carId;

    @NotBlank(message = "Passport is mandatory")
    String passport;

    @NotNull
    Boolean insurance;

    @NotNull
    LocalDateTime startRentalDate;

    @NotNull
    LocalDateTime endRentalDate;

}