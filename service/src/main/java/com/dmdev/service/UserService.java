package com.dmdev.service;

import com.dmdev.domain.UserDetailsImpl;
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
import com.dmdev.utils.predicate.UserPredicateBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final UserResponseMapper userResponseMapper;
    private final UserPredicateBuilder userPredicateBuilder;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Optional<UserResponseDto> create(UserCreateRequestDto userRequestDto) {
        this.checkEmailIsUnique(userRequestDto.getEmail());
        this.checkUsernameIsUnique(userRequestDto.getUsername());

        return Optional.of(userCreateMapper.mapToEntity(userRequestDto))
                .map(userRepository::save)
                .map(userResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<UserResponseDto> login(LoginRequestDto loginRequestDto) {
        return userRepository.findByEmailAndPassword(loginRequestDto.getEmail(), loginRequestDto.getPassword())
                .map(userResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<UserResponseDto> update(Long id, UserUpdateRequestDto user) {
        var existingUser = getByIdOrElseThrow(id);

        if (!existingUser.getEmail().equals(user.getEmail())) {
            checkEmailIsUnique(user.getEmail());
        }

        if (!existingUser.getUsername().equals(user.getUsername())) {
            checkUsernameIsUnique(user.getUsername());
        }

        return Optional.of(userUpdateMapper.mapToEntity(user, existingUser))
                .map(userRepository::save)
                .map(userResponseMapper::mapToDto);
    }


    public Optional<UserResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(userResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<UserResponseDto> changePassword(Long id, UserChangePasswordDto changedPasswordDto) {
        var existingUser = getByIdOrElseThrow(id);

        if (isExistsByEmailAndPassword(existingUser.getEmail(), passwordEncoder.encode(changedPasswordDto.getOldPassword()))) {
            existingUser.setPassword(passwordEncoder.encode(changedPasswordDto.getNewPassword())
            );
        }

        return Optional.of(userRepository.save(existingUser))
                .map(userResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<UserResponseDto> changeRole(Long id, Role role) {
        var existingUser = getByIdOrElseThrow(id);
        Optional.of(role).ifPresent(existingUser::setRole);
        return Optional.of(userRepository.save(existingUser))
                .map(userResponseMapper::mapToDto);
    }


    public Page<UserResponseDto> getAll(UserFilter userFilter, Integer page, Integer pageSize) {
        return userFilter.getAllExpiredLicenses() == null || !userFilter.getAllExpiredLicenses()
                ? userRepository.findAll(userPredicateBuilder.build(userFilter), PageableUtils.getSortedPageable(page, pageSize, Sort.Direction.ASC, "userDetails_surname")).map(userResponseMapper::mapToDto)
                : userRepository.findAllWithExpiredDriverLicense(LocalDate.now(), PageableUtils.unSortedPageable(page, pageSize)).map(userResponseMapper::mapToDto);
    }

    public List<UserResponseDto> getAllWithoutPage() {
             return userRepository.findAll().stream()
                     .map(userResponseMapper::mapToDto)
                     .collect(toList());
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
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("User", "id", id)));
    }

    public void checkUsernameIsUnique(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserBadRequestException(String.format(ExceptionMessageUtil.getAlreadyExistsMessage("User", "username", username)));
        }
    }
    public void checkEmailIsUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserBadRequestException(String.format(ExceptionMessageUtil.getAlreadyExistsMessage("User", "email", email)));
        }
    }

    private boolean isExistsByEmailAndPassword(String email, String password) {
        return userRepository.existsByEmailAndPassword(email, password);
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new UserDetailsImpl(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singleton(user.getRole()),
                        user.getId(),
                        user.getUsername()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user with email : " + email));
    }
}