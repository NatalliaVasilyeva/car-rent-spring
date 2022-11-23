package com.dmdev.service;

import com.dmdev.domain.dto.filterdto.UserDetailsFilter;
import com.dmdev.domain.dto.userdetails.request.UserDetailsCreateRequestDto;
import com.dmdev.domain.dto.userdetails.request.UserDetailsUpdateRequestDto;
import com.dmdev.domain.dto.userdetails.response.UserDetailsResponseDto;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.mapper.userdetails.UserDetailsCreateMapper;
import com.dmdev.mapper.userdetails.UserDetailsResponseMapper;
import com.dmdev.mapper.userdetails.UserDetailsUpdateMapper;
import com.dmdev.repository.UserDetailsRepository;
import com.dmdev.repository.UserRepository;
import com.dmdev.service.exception.ExceptionMessageUtil;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.utils.predicate.UserDetailsPredicateBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;
    private final UserDetailsCreateMapper userDetailsCreateMapper;
    private final UserDetailsUpdateMapper userDetailsUpdateMapper;
    private final UserDetailsResponseMapper userDetailsResponseMapper;
    private final UserDetailsPredicateBuilder userDetailsPredicateBuilder;

    @Transactional
    public Optional<UserDetailsResponseDto> create(UserDetailsCreateRequestDto userDetailsCreateRequestDtoRequestDto) {
        var existingUser = getUserByIdOrElseThrow(userDetailsCreateRequestDtoRequestDto.getUserId());
        var userDetails = userDetailsCreateMapper.mapToEntity(userDetailsCreateRequestDtoRequestDto);
        userDetails.setUser(existingUser);

        return Optional.of(userDetails)
                .map(userDetailsRepository::save)
                .map(userDetailsResponseMapper::mapToDto);

    }

    @Transactional
    public Optional<UserDetailsResponseDto> update(Long id, UserDetailsUpdateRequestDto userDetails) {
        var existingUserDetails = getByIdOrElseThrow(id);

        return Optional.of(userDetailsUpdateMapper.mapToEntity(userDetails, existingUserDetails))
                .map(userDetailsRepository::save)
                .map(userDetailsResponseMapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public Optional<UserDetailsResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(userDetailsResponseMapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public Page<UserDetailsResponseDto> getAll(UserDetailsFilter userDetailsFilter, Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "surname");
        return userDetailsRepository.findAll(
                        userDetailsPredicateBuilder
                                .build(userDetailsFilter), pageRequest)
                .map(userDetailsResponseMapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<UserDetailsResponseDto> getAllByNameAndSurname(String name, String surname) {
        return userDetailsRepository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(name, surname).stream()
                .map(userDetailsResponseMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDetailsResponseDto> getAllByRegistrationDate(LocalDate registrationDate) {
        return userDetailsRepository.findByRegistrationDate(registrationDate).stream()
                .map(userDetailsResponseMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDetailsResponseDto> getAllByRegistrationDates(LocalDate start, LocalDate end) {
        return userDetailsRepository.findByRegistrationDateBetween(start, end).stream()
                .map(userDetailsResponseMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserDetailsResponseDto> getByUserId(Long userId) {
        return Optional.of(userDetailsRepository.findByUserId(userId))
                .map(userDetailsResponseMapper::mapToDto);
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (userDetailsRepository.existsById(id)) {
            userDetailsRepository.deleteById(id);
            return true;
        }
        return false;
    }


    private User getUserByIdOrElseThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("User",  "id", id)));
    }

    private UserDetails getByIdOrElseThrow(Long id) {
        return userDetailsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("User details",  "id", id)));
    }
}