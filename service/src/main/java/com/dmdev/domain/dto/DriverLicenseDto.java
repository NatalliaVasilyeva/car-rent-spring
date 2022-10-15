package com.dmdev.domain.dto;

import lombok.Value;

import java.time.LocalDate;

@Value
public class DriverLicenseDto {
    String name;
    String surname;
    String phone;
    String number;
    LocalDate issueDate;
    LocalDate expiredDate;
}