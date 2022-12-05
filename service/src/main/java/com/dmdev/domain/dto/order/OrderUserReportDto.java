package com.dmdev.domain.dto.order;

import com.dmdev.domain.model.Color;
import com.dmdev.domain.model.EngineType;
import com.dmdev.domain.model.OrderStatus;
import com.dmdev.domain.model.Transmission;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
@Builder
public class OrderUserReportDto {

    @NotEmpty
    Long id;

    LocalDate date;
    LocalDateTime startRentalDate;
    LocalDateTime endRentalDate;
    String brand;
    String model;
    Color color;
    Transmission transmission;
    EngineType engineType;
    Integer yearOfProduction;
    Boolean insurance;
    OrderStatus orderStatus;
    BigDecimal sum;
}