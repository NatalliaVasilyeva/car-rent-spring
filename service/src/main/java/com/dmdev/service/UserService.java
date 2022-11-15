package com.dmdev.service;

import com.dmdev.domain.dto.filterdto.UserFilter;
import com.dmdev.domain.dto.user.request.LoginRequestDto;
import com.dmdev.domain.dto.user.request.UserChangePasswordDto;
import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.dto.user.request.UserUpdateRequestDto;
import com.dmdev.domain.dto.user.response.UserResponseDto;
import com.dmdev.domain.entity.User;
import com.dmdev.mapper.user.UserCreateMapper;
import com.dmdev.mapper.user.UserResponserMapper;
import com.dmdev.mapper.user.UserUpdateMapper;
import com.dmdev.repository.UserRepository;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.service.exception.UserBadRequestException;
import com.dmdev.utils.SecurityUtils;
import com.dmdev.utils.predicate.UserPredicateBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final UserResponserMapper userResponserMapper;
    private final UserPredicateBuilder userPredicateBuilder;

    @Transactional
    public Optional<UserResponseDto> create(UserCreateRequestDto userRequestDto) {
        this.checkEmailIsUnique(userRequestDto.getEmail());

        var user = userCreateMapper.map(userRequestDto);
        return Optional.of(userResponserMapper.map(userRepository.save(user)));
    }

    @Transactional
    public Optional<UserResponseDto> login(LoginRequestDto loginRequestDto) {
        return userRepository.findByEmailAndPassword(loginRequestDto.getEmail(),
                        SecurityUtils.securePassword(loginRequestDto.getEmail(), loginRequestDto.getPassword()))
                .map(userResponserMapper::map);
    }

    @Transactional
    public Optional<UserResponseDto> update(Long id, UserUpdateRequestDto user) {
        var existingUser = getByIdOrElseThrow(id);

        if (!existingUser.getEmail().equals(user.getEmail())) {
            checkEmailIsUnique(user.getEmail());
        }

        return Optional.of(
                        userRepository.save(
                                userUpdateMapper.map(user, existingUser)))
                .map(userResponserMapper::map);
    }
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(userResponserMapper::map);
    }

    public Optional<UserResponseDto> changePassword(Long id, UserChangePasswordDto changedPasswordDto) {
        var existingUser = getByIdOrElseThrow(id);

        if (isExistsByEmailAndPassword(existingUser.getEmail(),
                SecurityUtils.securePassword(existingUser.getEmail(), changedPasswordDto.getOldPassword()))) {
            existingUser.setPassword(
                    SecurityUtils.securePassword(existingUser.getEmail(), changedPasswordDto.getNewPassword())
            );
        }

        return Optional.of(userRepository.save(existingUser))
                .map(userResponserMapper::map);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAll(Integer page, Integer pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "userDetails_surname");
        return userRepository.findAll(pageRequest)
                .map(userResponserMapper::map);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllByFilter(UserFilter userFilter, Integer page, Integer pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "userDetails_surname");
        return userRepository.findAll(userPredicateBuilder.build(userFilter), pageRequest)
                .map(userResponserMapper::map);
    }


    @Transactional
    public boolean deleteById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private User getByIdOrElseThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s does not exist.", id)));
    }

    public void checkEmailIsUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserBadRequestException(String.format("User with email '%s' already exists", email));
        }
    }

    private boolean isExistsByEmailAndPassword(String email, String password) {
        return userRepository.existsByEmailAndPassword(email, password);
    }
}