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
import com.dmdev.service.exception.ExceptionMessageUtil;
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
@Transactional(readOnly = true)
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
            throw new DriverLicenseBadRequestException("Can not to create driver license for user." + ExceptionMessageUtil.getNotFoundMessage("User", "id", driverLicenseCreateRequestDto.getUserId()));
        } else {
            var user = optionalUser.get();
            checkDriverLicenseNumberIsUnique(driverLicenseCreateRequestDto.getDriverLicenseNumber());
            var driverLicense = driverLicenseCreateMapper.mapToEntity(driverLicenseCreateRequestDto);
            user.getUserDetails().setDriverLicense(driverLicense);

            return Optional.of(driverLicense)
                    .map(driverLicenseRepository::save)
                    .map(driverLicenseResponseMapper::mapToDto);
        }
    }

    @Transactional
    public Optional<DriverLicenseResponseDto> update(Long id, DriverLicenseUpdateRequestDto driverLicenseUpdateRequestDto) {
        var existingDriverLicense = getUserByIdOrElseThrow(id);

        if (!existingDriverLicense.getNumber().equals(driverLicenseUpdateRequestDto.getDriverLicenseNumber())) {
            checkDriverLicenseNumberIsUnique(driverLicenseUpdateRequestDto.getDriverLicenseNumber());
        }

        return Optional.of(driverLicenseUpdateMapper.mapToEntity(driverLicenseUpdateRequestDto, existingDriverLicense))
                .map(driverLicenseRepository::save)
                .map(driverLicenseResponseMapper::mapToDto);
    }


    public Optional<DriverLicenseResponseDto> getById(Long id) {
        return Optional.of(getUserByIdOrElseThrow(id))
                .map(driverLicenseResponseMapper::mapToDto);
    }


    public Page<DriverLicenseResponseDto> getAll(Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "number");
        return driverLicenseRepository.findAll(pageRequest)
                .map(driverLicenseResponseMapper::mapToDto);
    }


    public List<DriverLicenseResponseDto> getByNumber(String number) {
        return driverLicenseRepository.findByNumberContainingIgnoreCase(number).stream()
                .map(driverLicenseResponseMapper::mapToDto)
                .collect(Collectors.toList());
    }


    public Optional<DriverLicenseResponseDto> getByUserId(Long userId) {
        return driverLicenseRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(DriverLicense::getExpiredDate).reversed())
                .findFirst()
                .map(driverLicenseResponseMapper::mapToDto);
    }


    public List<DriverLicenseResponseDto> getAllExpiredDriverLicenses() {
        return driverLicenseRepository.findByExpiredDateLessThanEqual(LocalDate.now()).stream()
                .sorted(Comparator.comparing(DriverLicense::getExpiredDate).reversed())
                .map(driverLicenseResponseMapper::mapToDto)
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
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Driver license", "id", id)));
    }

    private void checkDriverLicenseNumberIsUnique(String licenseNumber) {
        if (driverLicenseRepository.existsByNumber(licenseNumber)) {
            throw new DriverLicenseBadRequestException(String.format(ExceptionMessageUtil.getAlreadyExistsMessage("Driver license", "number", licenseNumber)));
        }
    }
}