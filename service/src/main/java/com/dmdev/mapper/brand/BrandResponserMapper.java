package com.dmdev.mapper.brand;

import com.dmdev.domain.dto.brand.response.BrandResponseDto;
import com.dmdev.domain.entity.Brand;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class BrandResponserMapper implements Mapper<Brand, BrandResponseDto> {

    @Override
    public BrandResponseDto map(Brand brand) {
        return BrandResponseDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .models(brand.getModels())
                .build();
    }

    public BrandResponseDto mapNames(Brand brand) {
        return BrandResponseDto.builder()
                .name(brand.getName())
                .build();
    }
}