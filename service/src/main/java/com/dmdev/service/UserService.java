package com.dmdev.service;

import com.dmdev.domain.dto.filterdto.UserFilter;
import com.dmdev.domain.dto.user.request.LoginRequestDto;
import com.dmdev.domain.dto.user.request.UserChangePasswordDto;
import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.dto.user.request.UserUpdateRequestDto;
import com.dmdev.domain.dto.user.response.UserResponseDto;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.model.Role;
import com.dmdev.mapper.user.UserCreateMapper;
import com.dmdev.mapper.user.UserResponseMapper;
import com.dmdev.mapper.user.UserUpdateMapper;
import com.dmdev.repository.UserRepository;
import com.dmdev.service.exception.ExceptionMessageUtil;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.service.exception.UserBadRequestException;
import com.dmdev.utils.PageableUtils;
import com.dmdev.utils.SecurityUtils;
import com.dmdev.utils.predicate.UserPredicateBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final UserResponseMapper userResponseMapper;
    private final UserPredicateBuilder userPredicateBuilder;

    @Transactional
    public Optional<UserResponseDto> create(UserCreateRequestDto userRequestDto) {
        this.checkUsernameIsUnique(userRequestDto.getUsername());
        this.checkEmailIsUnique(userRequestDto.getEmail());

        return Optional.of(userCreateMapper.map(userRequestDto))
                .map(userRepository::save)
                .map(userResponseMapper::map);
    }

    @Transactional
    public Optional<UserResponseDto> login(LoginRequestDto loginRequestDto) {
        return userRepository.findByUsernameAndPassword(loginRequestDto.getUsername(),
                        SecurityUtils.securePassword(loginRequestDto.getUsername(), loginRequestDto.getPassword()))
                .map(userResponseMapper::map);
    }

    @Transactional
    public Optional<UserResponseDto> update(Long id, UserUpdateRequestDto user) {
        var existingUser = getByIdOrElseThrow(id);

        if (!existingUser.getEmail().equals(user.getEmail())) {
            checkUsernameIsUnique(user.getEmail());
        }

        return Optional.of(userUpdateMapper.map(user, existingUser))
                .map(userRepository::save)
                .map(userResponseMapper::map);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(userResponseMapper::map);
    }

    @Transactional
    public Optional<UserResponseDto> changePassword(Long id, UserChangePasswordDto changedPasswordDto) {
        var existingUser = getByIdOrElseThrow(id);

        if (isExistsByUsernameAndPassword(existingUser.getEmail(),
                SecurityUtils.securePassword(existingUser.getUsername(), changedPasswordDto.getOldPassword()))) {
            existingUser.setPassword(
                    SecurityUtils.securePassword(existingUser.getUsername(), changedPasswordDto.getNewPassword())
            );
        }

        return Optional.of(userRepository.save(existingUser))
                .map(userResponseMapper::map);
    }

    @Transactional
    public Optional<UserResponseDto> changeRole(Long id, Role role) {
        var existingUser = getByIdOrElseThrow(id);
        Optional.of(role).ifPresent(existingUser::setRole);
        return Optional.of(userRepository.save(existingUser))
                .map(userResponseMapper::map);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAll(UserFilter userFilter, Integer page, Integer pageSize) {
        return userFilter.getAllExpiredLicenses() == null || !userFilter.getAllExpiredLicenses()
                ? userRepository.findAll(userPredicateBuilder.build(userFilter), PageableUtils.getSortedPageable(page, pageSize, Sort.Direction.ASC, "userDetails_surname")).map(userResponseMapper::map)
                : userRepository.findAllWithExpiredDriverLicense(LocalDate.now(), PageableUtils.unSortedPageable(page, pageSize)).map(userResponseMapper::map);
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
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("User",  "id", id)));
    }

    public void checkUsernameIsUnique(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserBadRequestException(String.format(ExceptionMessageUtil.getAlreadyExistsMessage("User",  "username", username)));
        }
    }
    public void checkEmailIsUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserBadRequestException(String.format(ExceptionMessageUtil.getAlreadyExistsMessage("User",  "email", email)));
        }
    }

    private boolean isExistsByUsernameAndPassword(String email, String password) {
        return userRepository.existsByUsernameAndPassword(email, password);
    }
}