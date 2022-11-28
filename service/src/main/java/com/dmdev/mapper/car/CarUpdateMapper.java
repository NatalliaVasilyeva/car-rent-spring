package com.dmdev.mapper.car;

import com.dmdev.domain.dto.car.CarUpdateRequestDto;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.Model;
import com.dmdev.mapper.UpdateMapper;
import com.dmdev.repository.CategoryRepository;
import com.dmdev.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
@RequiredArgsConstructor
public class CarUpdateMapper implements UpdateMapper<CarUpdateRequestDto, Car> {

    private final ModelRepository modelRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Car mapToEntity(CarUpdateRequestDto requestDto, Car existing) {
        merge(requestDto, existing);
        return existing;
    }

    @Override
    public void merge(CarUpdateRequestDto requestDto, Car existing) {
        existing.setCategory(getCategory(requestDto.getCategoryId()));
        existing.setColor(requestDto.getColor());
        existing.setYear(requestDto.getYearOfProduction());
        existing.setCarNumber(requestDto.getNumber());
        existing.setRepaired(requestDto.getIsRepaired());

        if (!Objects.equals(requestDto.getModelId(), existing.getModel().getId())) {
            var model = getModel(requestDto.getModelId());
            existing.setModel(model);
        }

        Optional.ofNullable(requestDto.getImage())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> existing.setImage(image.getOriginalFilename()));
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
}