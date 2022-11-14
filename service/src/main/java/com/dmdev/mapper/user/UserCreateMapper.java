package com.dmdev.mapper.user;

import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.entity.User;
import com.dmdev.mapper.Mapper;
import com.dmdev.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCreateMapper implements Mapper<UserCreateRequestDto, User> {

    @Autowired
    private  UserDetailsCreateMapper userDetailsCreateMapper;

    @Autowired
    private  DriverLicenseCreateMapper driverLicenseCreateMapper;


    @Override
    public User map(UserCreateRequestDto requestDto) {
        var driverLicense = driverLicenseCreateMapper.map(requestDto);
        var userDetails = userDetailsCreateMapper.map(requestDto);
        var user = User.builder()
                .login(requestDto.getLogin())
                .email(requestDto.getEmail())
                .password(AppUtils.generateHash(requestDto.getEmail(), requestDto.getPassword()))
                .build();
        userDetails.setUser(user);
        userDetails.setDriverLicense(driverLicense);
        return user;
    }
}