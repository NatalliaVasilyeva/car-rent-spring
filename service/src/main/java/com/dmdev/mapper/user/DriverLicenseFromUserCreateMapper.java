package com.dmdev.mapper.user;

import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class DriverLicenseFromUserCreateMapper implements Mapper<UserCreateRequestDto, DriverLicense> {

    @Override
    public DriverLicense map(UserCreateRequestDto requestDto) {
        return DriverLicense.builder()
                .number(requestDto.getDriverLicenseNumber())
                .issueDate(requestDto.getDriverLicenseIssueDate())
                .expiredDate(requestDto.getDriverLicenseExpiredDate())
                .build();
    }
}