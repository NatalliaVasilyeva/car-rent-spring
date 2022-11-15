package com.dmdev.domain.dto.driverlicense.response;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Value
@Builder
public class DriverLicenseResponseDto {

    @NotEmpty
    Long id;
    String driverLicenseNumber;
    LocalDate driverLicenseIssueDate;
    LocalDate driverLicenseExpiredDate;
}