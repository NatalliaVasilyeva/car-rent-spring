package com.dmdev.domain.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public interface DriverLicenseFullView {

    Long getId();

    String getNumber();

    LocalDate getIssueDate();

    LocalDate getExpiredDate();

    String getFirstname();

    String getLastname();

    String getPhone();

    @Value("#target.firstname + ' ' + target.lastname")
    String getFullName();
}