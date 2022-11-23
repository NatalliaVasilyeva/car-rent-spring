package com.dmdev.mapper.driverlicense;

import com.dmdev.domain.dto.driverlicense.response.DriverLicenseResponseDto;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class DriverLicenseResponseMapper implements ResponseMapper<DriverLicense, DriverLicenseResponseDto> {

    @Override
    public DriverLicenseResponseDto mapToDto(DriverLicense driverLicense) {
        return DriverLicenseResponseDto.builder()
                .id(driverLicense.getId())
                .userId(driverLicense.getUserDetails().getUser().getId())
                .driverLicenseNumber(driverLicense.getNumber())
                .driverLicenseIssueDate(driverLicense.getIssueDate())
                .driverLicenseExpiredDate(driverLicense.getExpiredDate())
                .build();
    }
}