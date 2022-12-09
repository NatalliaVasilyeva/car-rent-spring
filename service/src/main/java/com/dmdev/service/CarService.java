package com.dmdev.service;

import com.dmdev.domain.dto.car.CarCreateRequestDto;
import com.dmdev.domain.dto.car.CarResponseDto;
import com.dmdev.domain.dto.car.CarUpdateRequestDto;
import com.dmdev.domain.dto.filterdto.CarFilter;
import com.dmdev.domain.entity.Car;
import com.dmdev.mapper.car.CarCreateMapper;
import com.dmdev.mapper.car.CarResponseMapper;
import com.dmdev.mapper.car.CarUpdateMapper;
import com.dmdev.repository.CarRepository;
import com.dmdev.service.exception.ExceptionMessageUtil;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.utils.PageableUtils;
import com.dmdev.utils.predicate.CarPredicateBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;
    private final CarCreateMapper carCreateMapper;
    private final CarUpdateMapper carUpdateMapper;
    private final CarResponseMapper carResponseMapper;
    private final ImageService imageService;
    private final CarPredicateBuilder carPredicateBuilder;

    @Transactional
    public Optional<CarResponseDto> create(CarCreateRequestDto carCreateRequestDto) {

        return Optional.of(carCreateRequestDto)
                .map(dto -> {
                    if (dto.getImage() != null) {
                        downloadImage(dto.getImage());
                    }
                    return carCreateMapper.mapToEntity(dto);
                })
                .map(carRepository::save)
                .map(carResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<CarResponseDto> update(Long id, CarUpdateRequestDto carUpdateRequestDto) {
        var existingCar = getByIdOrElseThrow(id);

        return Optional.of(carUpdateRequestDto)
                .map(dto -> {
                    if (dto.getImage() != null) {
                        downloadImage(dto.getImage());
                        imageService.delete(existingCar.getImage());
                    }
                    return carUpdateMapper.mapToEntity(dto, existingCar);
                })
                .map(carRepository::save)
                .map(carResponseMapper::mapToDto);
    }


    public Optional<CarResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(carResponseMapper::mapToDto);
    }


    public List<CarResponseDto> getAll() {
        return carRepository.findAll().stream()
                .map(carResponseMapper::mapToDto)
                .collect(toList());
    }


    public Page<CarResponseDto> getAll(Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "number");
        return carRepository.findAll(pageRequest)
                .map(carResponseMapper::mapToDto);
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public Optional<CarResponseDto> getByCarNumber(String number) {
        return carRepository.findByCarNumber(number)
                .map(carResponseMapper::mapToDto);
    }


    public List<CarResponseDto> getAllWithAccidents() {
        return carRepository.findAllWithAccidents()
                .stream()
                .map(carResponseMapper::mapToDto)
                .collect(toList());
    }


    public List<CarResponseDto> getAllWithoutAccidents() {
        return carRepository.findAllWithoutAccidents()
                .stream()
                .map(carResponseMapper::mapToDto)
                .collect(toList());
    }


    public List<CarResponseDto> getAllAvailable() {
        return carRepository.findAllAvailable()
                .stream()
                .map(carResponseMapper::mapToDto)
                .collect(toList());
    }


    public List<CarResponseDto> getAllAllUnderRepair() {
        return carRepository.findAllUnderRepair()
                .stream()
                .map(carResponseMapper::mapToDto)
                .collect(toList());
    }

    public Page<CarResponseDto> getAll(CarFilter carFilter, Integer page, Integer pageSize) {
        return carRepository.findAll(carPredicateBuilder.build(carFilter), PageableUtils.getSortedPageable(page, pageSize, Sort.Direction.ASC, "brand_name")).map(carResponseMapper::mapToDto);
    }

    public Optional<byte[]> findCarImage(Long id) {
        return carRepository.findById(id)
                .map(Car::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }


    private Car getByIdOrElseThrow(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Car", "id", id)));
    }


    @SneakyThrows
    private void downloadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }
}