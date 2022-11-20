package com.dmdev.service;

import com.dmdev.domain.dto.driverlicense.request.DriverLicenseCreateRequestDto;
import com.dmdev.domain.dto.driverlicense.request.DriverLicenseUpdateRequestDto;
import com.dmdev.domain.dto.driverlicense.response.DriverLicenseResponseDto;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.mapper.driverlicense.DriverLicenseCreateMapper;
import com.dmdev.mapper.driverlicense.DriverLicenseResponseMapper;
import com.dmdev.mapper.driverlicense.DriverLicenseUpdateMapper;
import com.dmdev.repository.DriverLicenseRepository;
import com.dmdev.repository.UserRepository;
import com.dmdev.service.exception.DriverLicenseBadRequestException;
import com.dmdev.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverLicenseService {

    private final DriverLicenseRepository driverLicenseRepository;
    private final UserRepository userRepository;
    private final DriverLicenseCreateMapper driverLicenseCreateMapper;
    private final DriverLicenseUpdateMapper driverLicenseUpdateMapper;
    private final DriverLicenseResponseMapper driverLicenseResponseMapper;

    @Transactional
    public Optional<DriverLicenseResponseDto> create(DriverLicenseCreateRequestDto driverLicenseCreateRequestDto) {
        var optionalUser = userRepository.findById(driverLicenseCreateRequestDto.getUserId());
        if (optionalUser.isEmpty()) {
            throw new DriverLicenseBadRequestException(String.format("Can not to create driver license for user. User with id '%s' does not  exists", driverLicenseCreateRequestDto.getUserId()));
        } else {
            var user = optionalUser.get();
            checkDriverLicenseNumberIsUnique(driverLicenseCreateRequestDto.getDriverLicenseNumber());
            var driverLicense = driverLicenseCreateMapper.map(driverLicenseCreateRequestDto);
            user.getUserDetails().setDriverLicense(driverLicense);
            return Optional.of(driverLicenseResponseMapper
                    .map(driverLicenseRepository
                            .save(driverLicense)));
        }
    }

    @Transactional
    public Optional<DriverLicenseResponseDto> update(Long id, DriverLicenseUpdateRequestDto driverLicenseUpdateRequestDto) {
        var existingDriverLicense = getUserByIdOrElseThrow(id);

        if (!existingDriverLicense.getNumber().equals(driverLicenseUpdateRequestDto.getDriverLicenseNumber())) {
            checkDriverLicenseNumberIsUnique(driverLicenseUpdateRequestDto.getDriverLicenseNumber());
        }

        return Optional.of(
                        driverLicenseRepository.save(
                                driverLicenseUpdateMapper.map(driverLicenseUpdateRequestDto, existingDriverLicense)))
                .map(driverLicenseResponseMapper::map);
    }

    @Transactional(readOnly = true)
    public Optional<DriverLicenseResponseDto> getById(Long id) {
        return Optional.of(getUserByIdOrElseThrow(id))
                .map(driverLicenseResponseMapper::map);
    }

    @Transactional(readOnly = true)
    public Page<DriverLicenseResponseDto> getAll(Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "number");
        return driverLicenseRepository.findAll(pageRequest)
                .map(driverLicenseResponseMapper::map);
    }

    @Transactional(readOnly = true)
    public List<DriverLicenseResponseDto> getByNumber(String number) {
        return driverLicenseRepository.findByNumberContainingIgnoreCase(number)
                .stream()
                .map(driverLicenseResponseMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<DriverLicenseResponseDto> getByUserId(Long userId) {
        return driverLicenseRepository.findByUserId(userId)
                .stream()
                .sorted(Comparator.comparing(DriverLicense::getExpiredDate).reversed())
                .limit(1)
                .findFirst()
                .map(driverLicenseResponseMapper::map);
    }

    @Transactional(readOnly = true)
    public List<DriverLicenseResponseDto> getAllExpiredDriverLicenses() {
        return driverLicenseRepository.findByExpiredDateLessThanEqual(LocalDate.now())
                .stream()
                .sorted(Comparator.comparing(DriverLicense::getExpiredDate).reversed())
                .map(driverLicenseResponseMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (driverLicenseRepository.existsById(id)) {
            driverLicenseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private DriverLicense getUserByIdOrElseThrow(Long id) {
        return driverLicenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Driver license with id %s does not exist.", id)));
    }

    private void checkDriverLicenseNumberIsUnique(String licenseNumber) {
        if (driverLicenseRepository.existsByNumber(licenseNumber)) {
            throw new DriverLicenseBadRequestException(String.format("Driver license with number '%s' already exists", licenseNumber));
        }
    }
}