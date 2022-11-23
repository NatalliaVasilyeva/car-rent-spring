package com.dmdev.mapper.driverlicense;

import com.dmdev.domain.dto.category.request.CategoryCreateEditRequestDto;
import com.dmdev.domain.dto.driverlicense.request.DriverLicenseUpdateRequestDto;
import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.mapper.Mapper;
import com.dmdev.mapper.UpdateMapper;
import org.springframework.stereotype.Component;

@Component
public class DriverLicenseUpdateMapper implements UpdateMapper<DriverLicenseUpdateRequestDto, DriverLicense> {

    @Override
    public DriverLicense mapToEntity(DriverLicenseUpdateRequestDto requestDto, DriverLicense existing) {
        merge(requestDto, existing);
        return existing;
    }

    @Override
    public void merge(DriverLicenseUpdateRequestDto requestDto, DriverLicense existing) {
        existing.setNumber(requestDto.getDriverLicenseNumber());
        existing.setIssueDate(requestDto.getDriverLicenseIssueDate());
        existing.setExpiredDate(requestDto.getDriverLicenseExpiredDate());
    }
}