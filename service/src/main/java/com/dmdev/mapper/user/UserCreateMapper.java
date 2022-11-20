package com.dmdev.mapper.user;

import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.entity.User;
import com.dmdev.mapper.Mapper;
import com.dmdev.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateRequestDto, User> {

    private final UserDetailsFromUserCreateMapper userDetailsCreateMapper;
    private final DriverLicenseFromUserCreateMapper driverLicenseCreateMapper;

    @Override
    public User map(UserCreateRequestDto requestDto) {
        var driverLicense = driverLicenseCreateMapper.map(requestDto);
        var userDetails = userDetailsCreateMapper.map(requestDto);
        var user = User.builder()
                .login(requestDto.getLogin())
                .email(requestDto.getEmail())
                .password(SecurityUtils.securePassword(requestDto.getEmail(), requestDto.getPassword()))
                .build();
        userDetails.setUser(user);
        userDetails.setDriverLicense(driverLicense);
        return user;
    }
}