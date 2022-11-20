package com.dmdev.mapper.driverlicense;

import com.dmdev.domain.dto.driverlicense.response.DriverLicenseResponseDto;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class DriverLicenseResponseMapper implements Mapper<DriverLicense, DriverLicenseResponseDto> {

    @Override
    public DriverLicenseResponseDto map(DriverLicense driverLicense) {
        return DriverLicenseResponseDto.builder()
                .id(driverLicense.getId())
                .userId(driverLicense.getUserDetails().getUser().getId())
                .driverLicenseNumber(driverLicense.getNumber())
                .driverLicenseIssueDate(driverLicense.getIssueDate())
                .driverLicenseExpiredDate(driverLicense.getExpiredDate())
                .build();
    }
}