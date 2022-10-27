package com.dmdev.domain.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class DriverLicenseFilter {
    LocalDate issueDate;
    LocalDate expiredDate;
    String phone;
}