package com.dmdev.mapper.user;

import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.mapper.CreateMapper;
import org.springframework.stereotype.Component;

@Component
public class DriverLicenseFromUserCreateMapper implements CreateMapper<UserCreateRequestDto, DriverLicense> {

    @Override
    public DriverLicense mapToEntity(UserCreateRequestDto requestDto) {
        return DriverLicense.builder()
                .number(requestDto.getDriverLicenseNumber())
                .issueDate(requestDto.getDriverLicenseIssueDate())
                .expiredDate(requestDto.getDriverLicenseExpiredDate())
                .build();
    }
}