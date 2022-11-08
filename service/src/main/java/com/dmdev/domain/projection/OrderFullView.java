package com.dmdev.domain.projection;

import com.dmdev.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface OrderFullView {

    Long getId();

    LocalDate getDate();

    boolean getInsurance();

    OrderStatus getOrderStatus();

    BigDecimal getSum();

    LocalDateTime getStartRentalDate();

    LocalDateTime getEndRentalDate();

    String getCarNumber();

    String getBrandName();

    String getModelName();

    String getFirstname();

    String getSurname();

    String getPhone();

    boolean getAccidents();
}