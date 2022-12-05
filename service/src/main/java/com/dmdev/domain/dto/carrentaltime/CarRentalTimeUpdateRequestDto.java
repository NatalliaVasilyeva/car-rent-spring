package com.dmdev.domain.dto.carrentaltime;

import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class CarRentalTimeUpdateRequestDto {

    @NotNull
    LocalDateTime startRentalDate;

    @NotNull
    LocalDateTime endRentalDate;

}