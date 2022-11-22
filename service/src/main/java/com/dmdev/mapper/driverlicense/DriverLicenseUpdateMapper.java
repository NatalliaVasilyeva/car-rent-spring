package com.dmdev.mapper.driverlicense;

import com.dmdev.domain.dto.driverlicense.request.DriverLicenseUpdateRequestDto;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class DriverLicenseUpdateMapper implements Mapper<DriverLicenseUpdateRequestDto, DriverLicense> {

    @Override
    public DriverLicense map(DriverLicenseUpdateRequestDto object) {
        return DriverLicense.builder()
                .number(object.getDriverLicenseNumber())
                .issueDate(object.getDriverLicenseIssueDate())
                .expiredDate(object.getDriverLicenseExpiredDate())
                .build();
    }

    @Override
    public DriverLicense map(DriverLicenseUpdateRequestDto requestDto, DriverLicense existing) {
        merge(requestDto, existing);
        return existing;
    }

    private void merge(DriverLicenseUpdateRequestDto requestDto, DriverLicense existing) {
        existing.setNumber(requestDto.getDriverLicenseNumber());
        existing.setIssueDate(requestDto.getDriverLicenseIssueDate());
        existing.setExpiredDate(requestDto.getDriverLicenseExpiredDate());
    }
}