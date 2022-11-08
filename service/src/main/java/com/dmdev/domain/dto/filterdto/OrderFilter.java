package com.dmdev.domain.dto.filterdto;

import com.dmdev.domain.model.OrderStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class OrderFilter {

    String userFirstName;
    String userLastName;
    String carNumber;
    OrderStatus orderStatus;
    BigDecimal sum;
}