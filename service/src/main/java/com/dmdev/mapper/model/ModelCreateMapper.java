package com.dmdev.mapper.model;

import com.dmdev.domain.dto.model.ModelCreateRequestDto;
import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Model;
import com.dmdev.mapper.CreateMapper;
import com.dmdev.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ModelCreateMapper implements CreateMapper<ModelCreateRequestDto, Model> {

    private final BrandRepository brandRepository;

    public Model mapToEntity(ModelCreateRequestDto requestDto) {
        var brand = getBrand(requestDto.getBrandId());
        var model = Model.builder()
                .name(requestDto.getName())
                .engineType(requestDto.getEngineType())
                .transmission(requestDto.getTransmission())
                .build();

        brand.setModel(model);
        return model;
    }

    private Brand getBrand(Long brandId) {
        return Optional.ofNullable(brandId)
                .flatMap(brandRepository::findById)
                .orElse(null);
    }
}