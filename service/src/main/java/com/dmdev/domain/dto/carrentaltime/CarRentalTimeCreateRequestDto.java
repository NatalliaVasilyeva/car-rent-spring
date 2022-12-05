package com.dmdev.domain.dto.carrentaltime;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class CarRentalTimeCreateRequestDto {

    @NotNull
    Long orderId;

    @NotNull
    LocalDateTime startRentalDate;

    @NotNull
    LocalDateTime endRentalDate;

}