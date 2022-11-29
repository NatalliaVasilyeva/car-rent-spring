package com.dmdev.mapper.car;

import com.dmdev.domain.dto.car.CarCreateRequestDto;
import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.Model;
import com.dmdev.mapper.CreateMapper;
import com.dmdev.repository.BrandRepository;
import com.dmdev.repository.CategoryRepository;
import com.dmdev.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
@RequiredArgsConstructor
public class CarCreateMapper implements CreateMapper<CarCreateRequestDto, Car> {

    private final ModelRepository modelRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public Car mapToEntity(CarCreateRequestDto requestDto) {
        var car = Car.builder()
                .color(requestDto.getColor())
                .year(requestDto.getYearOfProduction())
                .carNumber(requestDto.getNumber())
                .vin(requestDto.getVin())
                .repaired(requestDto.getIsRepaired())
                .build();

        car.setCategory(getCategory(requestDto.getCategoryId()));
        car.setBrand(getBrand(requestDto.getBrandId()));
        car.setModel(getModel(requestDto.getModelId()));

        Optional.ofNullable(requestDto.getImage())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> car.setImage(image.getOriginalFilename()));

        return car;
    }

    private Model getModel(Long modelId) {
        return Optional.ofNullable(modelId)
                .flatMap(modelRepository::findById)
                .orElse(null);
    }

    private Category getCategory(Long categoryId) {
        return Optional.ofNullable(categoryId)
                .flatMap(categoryRepository::findById)
                .orElse(null);
    }

    private Brand getBrand(Long brandId) {
        return Optional.ofNullable(brandId)
                .flatMap(brandRepository::findById)
                .orElse(null);
    }
}