package com.dmdev.service;

import com.dmdev.domain.dto.carrentaltime.CarRentalTimeCreateRequestDto;
import com.dmdev.domain.dto.carrentaltime.CarRentalTimeResponseDto;
import com.dmdev.domain.dto.carrentaltime.CarRentalTimeUpdateRequestDto;
import com.dmdev.domain.dto.filterdto.UserDetailsFilter;
import com.dmdev.domain.dto.userdetails.request.UserDetailsCreateRequestDto;
import com.dmdev.domain.dto.userdetails.request.UserDetailsUpdateRequestDto;
import com.dmdev.domain.dto.userdetails.response.UserDetailsResponseDto;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.domain.entity.Order;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.mapper.carrentaltime.CarRentalTimeCreateMapper;
import com.dmdev.mapper.carrentaltime.CarRentalTimeResponseMapper;
import com.dmdev.mapper.carrentaltime.CarRentalTimeUpdateMapper;
import com.dmdev.mapper.driverlicense.DriverLicenseCreateMapper;
import com.dmdev.mapper.driverlicense.DriverLicenseResponseMapper;
import com.dmdev.mapper.driverlicense.DriverLicenseUpdateMapper;
import com.dmdev.repository.CarRentalTimeRepository;
import com.dmdev.repository.OrderRepository;
import com.dmdev.service.exception.ExceptionMessageUtil;
import com.dmdev.service.exception.NotFoundException;
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

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarRentalTimeService {

    private final CarRentalTimeRepository carRentalTimeRepository;
    private final OrderRepository orderRepository;
    private final CarRentalTimeCreateMapper carRentalTimeCreateMapper;
    private final CarRentalTimeUpdateMapper carRentalTimeUpdateMapper;
    private final CarRentalTimeResponseMapper carRentalTimeResponseMapper;



    @Transactional
    public Optional<CarRentalTimeResponseDto> create(CarRentalTimeCreateRequestDto carRentalTimeCreateRequestDto) {
        var existingOrder = getOrderByIdOrElseThrow(carRentalTimeCreateRequestDto.getOrderId());
        var carRentalTime = carRentalTimeCreateMapper.mapToEntity(carRentalTimeCreateRequestDto);
        carRentalTime.setOrder(existingOrder);

        return Optional.of(carRentalTime)
                .map(carRentalTimeRepository::save)
                .map(carRentalTimeResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<CarRentalTimeResponseDto> update(Long id, CarRentalTimeUpdateRequestDto carRentalTimeUpdateRequestDto) {
        var existingCarRentalTime = getByIdOrElseThrow(id);

        return Optional.of(carRentalTimeUpdateMapper.mapToEntity(carRentalTimeUpdateRequestDto, existingCarRentalTime))
                .map(carRentalTimeRepository::save)
                .map(carRentalTimeResponseMapper::mapToDto);
    }


    public Optional<CarRentalTimeResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(carRentalTimeResponseMapper::mapToDto);
    }


    public List<CarRentalTimeResponseDto> getAll() {
        return carRentalTimeRepository.findAll()
                .stream()
                .map(carRentalTimeResponseMapper::mapToDto)
                .collect(toList());
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (carRentalTimeRepository.existsById(id)) {
            carRentalTimeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Order getOrderByIdOrElseThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Order", "id", id)));
    }

    private CarRentalTime getByIdOrElseThrow(Long id) {
        return carRentalTimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Car rental time", "id", id)));
    }
}