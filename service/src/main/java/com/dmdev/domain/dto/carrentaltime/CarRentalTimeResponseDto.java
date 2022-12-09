package com.dmdev.domain.dto.carrentaltime;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Value
@Builder
public class CarRentalTimeResponseDto {

    @NotEmpty
    Long id;

    @NotEmpty
    Long orderId;
    LocalDateTime startRentalDate;
    LocalDateTime endRentalDate;
}