package com.dmdev.mapper.driverlicense;

import com.dmdev.domain.dto.driverlicense.request.DriverLicenseCreateRequestDto;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.mapper.CreateMapper;
import org.springframework.stereotype.Component;

@Component
public class DriverLicenseCreateMapper implements CreateMapper<DriverLicenseCreateRequestDto, DriverLicense> {

    @Override
    public DriverLicense mapToEntity(DriverLicenseCreateRequestDto requestDto) {
        return DriverLicense.builder()
                .number(requestDto.getDriverLicenseNumber())
                .issueDate(requestDto.getDriverLicenseIssueDate())
                .expiredDate(requestDto.getDriverLicenseExpiredDate())
                .userDetails(UserDetails.builder()
                        .user(User.builder()
                                .id(requestDto.getUserId())
                                .build())
                        .build())
                .build();
    }
}