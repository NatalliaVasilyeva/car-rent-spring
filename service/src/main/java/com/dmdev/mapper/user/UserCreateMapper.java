package com.dmdev.mapper.user;

import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.entity.User;
import com.dmdev.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateRequestDto, User> {

    private final UserDetailsFromUserCreateMapper userDetailsCreateMapper;
    private final DriverLicenseFromUserCreateMapper driverLicenseCreateMapper;
    private final PasswordEncoder passwordEncoder;

    public User mapToEntity(UserCreateRequestDto requestDto) {
        var driverLicense = driverLicenseCreateMapper.mapToEntity(requestDto);
        var userDetails = userDetailsCreateMapper.mapToEntity(requestDto);
        var user = User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        userDetails.setUser(user);
        userDetails.setDriverLicense(driverLicense);
        return user;
    }
}