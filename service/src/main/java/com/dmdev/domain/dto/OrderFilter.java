package com.dmdev.domain.dto;

import com.dmdev.domain.model.OrderStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class OrderFilter {
    LocalDate date;
    String userEmail;
    String userFirstName;
    String userLastName;
    String carNumber;
    String brandName;
    String modelName;
    OrderStatus orderStatus;
    BigDecimal sum;
    boolean insurance;
}