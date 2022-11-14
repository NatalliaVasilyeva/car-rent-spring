package com.dmdev.service;

import com.dmdev.domain.dto.filterdto.UserFilter;
import com.dmdev.domain.dto.user.request.LoginRequestDto;
import com.dmdev.domain.dto.user.request.UserChangePasswordDto;
import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.dto.user.request.UserUpdateRequestDto;
import com.dmdev.domain.dto.user.response.UserResponseDto;
import com.dmdev.domain.entity.User;
import com.dmdev.mapper.user.DriverLicenseCreateMapper;
import com.dmdev.mapper.user.UserCreateMapper;
import com.dmdev.mapper.user.UserDetailsCreateMapper;
import com.dmdev.mapper.user.UserResponserMapper;
import com.dmdev.mapper.user.UserUpdateMapper;
import com.dmdev.repository.UserRepository;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.service.exception.UserBadRequestException;
import com.dmdev.utils.AppUtils;
import com.dmdev.utils.predicate.UserPredicateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final UserResponserMapper userResponserMapper;
    private final UserDetailsCreateMapper userDetailsCreateMapper;
    private final DriverLicenseCreateMapper driverLicenseCreateMapper;
    private final UserPredicateBuilder userPredicateBuilder;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserCreateMapper userCreateMapper,
                       UserUpdateMapper userUpdateMapper,
                       UserResponserMapper userResponserMapper,
                       UserDetailsCreateMapper userDetailsCreateMapper,
                       DriverLicenseCreateMapper driverLicenseCreateMapper,
                       UserPredicateBuilder userPredicateBuilder) {
        this.userRepository = userRepository;
        this.userUpdateMapper = userUpdateMapper;
        this.userCreateMapper = userCreateMapper;
        this.userResponserMapper = userResponserMapper;
        this.userDetailsCreateMapper = userDetailsCreateMapper;
        this.driverLicenseCreateMapper = driverLicenseCreateMapper;
        this.userPredicateBuilder = userPredicateBuilder;
    }

    @Transactional
    public Optional<UserResponseDto> createUser(UserCreateRequestDto userRequestDto) {
        this.isUniqueEmail(userRequestDto.getEmail());

        var user = userCreateMapper.map(userRequestDto);
        user.setPassword(AppUtils.generateHash(user.getEmail(), user.getPassword()));
        var driverLicense = driverLicenseCreateMapper.map(userRequestDto);
        var userDetails = userDetailsCreateMapper.map(userRequestDto);
        userDetails.setUser(user);
        userDetails.setDriverLicense(driverLicense);
        return Optional.of(userResponserMapper.map(userRepository.save(user)));
    }

    @Transactional
    public Optional<UserResponseDto> login(LoginRequestDto loginRequestDto) {
        return userRepository.findByEmailAndPassword(loginRequestDto.getEmail(), AppUtils.generateHash(loginRequestDto.getEmail(), loginRequestDto.getPassword()))
                .map(userResponserMapper::map);
    }

    @Transactional
    public Optional<UserResponseDto> updateUser(Long id, UserUpdateRequestDto user) {
        var existingUser = getUserByIdOrElseThrow(id);

        if (!existingUser.getEmail().equals(user.getEmail())) {
            isUniqueEmail(user.getEmail());
        }

        return Optional.of(userRepository.save(userUpdateMapper.map(user, existingUser)))
                .map(userResponserMapper::map);
    }
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUser(Long id) {
        return Optional.of(getUserByIdOrElseThrow(id))
                .map(userResponserMapper::map);
    }

    public Optional<UserResponseDto> changePassword(Long id, UserChangePasswordDto changedPasswordDto) {
        var existingUser = getUserByIdOrElseThrow(id);

        if (isExistsByEmailAndPassword(existingUser.getEmail(),
                AppUtils.generateHash(existingUser.getEmail(), changedPasswordDto.getOldPassword()))) {
            existingUser.setPassword(AppUtils.generateHash(existingUser.getEmail(), changedPasswordDto.getNewPassword()));
        }

        return Optional.of(userRepository.save(existingUser))
                .map(userResponserMapper::map);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsers(Integer page, Integer pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "userDetails_surname");
        return userRepository.findAll(pageRequest)
                .map(userResponserMapper::map);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersByFilter(UserFilter userFilter, Integer page, Integer pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "userDetails_surname");
        return userRepository.findAll(userPredicateBuilder.build(userFilter), pageRequest)
                .map(userResponserMapper::map);
    }


    @Transactional
    public boolean deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private User getUserByIdOrElseThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s does not exist.", id)));
    }

    public void isUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserBadRequestException(String.format("User with email '%s' already exists", email));
        }
    }

    private boolean isExistsByEmailAndPassword(String email, String password) {
        return userRepository.existsByEmailAndPassword(email, password);
    }
}