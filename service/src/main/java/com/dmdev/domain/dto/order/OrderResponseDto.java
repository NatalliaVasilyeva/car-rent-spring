package com.dmdev.domain.dto.order;

import com.dmdev.domain.dto.car.CarResponseDto;
import com.dmdev.domain.dto.user.response.UserResponseDto;
import com.dmdev.domain.model.OrderStatus;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
@Builder
public class OrderResponseDto {

    @NotEmpty
    Long id;

    LocalDate date;
    LocalDateTime startRentalDate;
    LocalDateTime endRentalDate;
    CarResponseDto car;
    UserResponseDto user;
    Boolean insurance;
    OrderStatus orderStatus;
    BigDecimal sum;
}