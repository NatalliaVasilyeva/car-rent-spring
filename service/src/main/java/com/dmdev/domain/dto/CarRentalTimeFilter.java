package com.dmdev.domain.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class CarRentalTimeFilter {
    String orderId;
    LocalDateTime startRentalDate;
    LocalDateTime endRentalDate;

}