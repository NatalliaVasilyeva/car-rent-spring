package com.dmdev.domain.projection;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AccidentFullView {

    Long getId();

    LocalDate getAccidentDate();

    LocalDate getDescription();

    BigDecimal getDamage();

    Long getOrderId();

    String getBrandName();

    String getModelName();

    String getCarNumber();

    String getFirstname();

    String getSurname();

    @Value("#target.firstname + ' ' + target.lastname")
    String getFullName();
}