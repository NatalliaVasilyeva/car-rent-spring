package com.dmdev.domain.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class AccidentFilter {
    LocalDate accidentDate;
    BigDecimal damage;
    String carNumber;
    LocalDate orderDate;
}