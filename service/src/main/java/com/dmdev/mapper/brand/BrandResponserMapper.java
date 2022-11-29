package com.dmdev.mapper.brand;

import com.dmdev.domain.dto.brand.response.BrandResponseDto;
import com.dmdev.domain.entity.Brand;
import com.dmdev.mapper.ResponseMapper;
import com.dmdev.mapper.model.ModelResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandResponserMapper implements ResponseMapper<Brand, BrandResponseDto> {

    private final ModelResponseMapper modelResponseMapper;

    @Override
    public BrandResponseDto mapToDto(Brand brand) {
        return BrandResponseDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .models(modelResponseMapper.mapToDtos(brand.getModels()))
                .build();
    }
}