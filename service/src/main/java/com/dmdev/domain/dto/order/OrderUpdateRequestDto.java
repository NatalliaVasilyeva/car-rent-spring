package com.dmdev.domain.dto.order;

import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class OrderUpdateRequestDto {

    @NotNull
    Long carId;

    @NotNull
    Boolean insurance;

    @NotNull
    LocalDateTime startRentalDate;

    @NotNull
    LocalDateTime endRentalDate;
}