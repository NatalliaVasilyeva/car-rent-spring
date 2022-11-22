package com.dmdev.mapper.brand;

import com.dmdev.domain.dto.brand.request.BrandCreateEditRequestDto;
import com.dmdev.domain.entity.Brand;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class BrandUpdateMapper implements Mapper<BrandCreateEditRequestDto, Brand> {

    @Override
    public Brand map(BrandCreateEditRequestDto object) {
        return Brand.builder()
                .name(object.getName())
                .build();
    }

    @Override
    public Brand map(BrandCreateEditRequestDto requestDto, Brand existing) {
        merge(requestDto, existing);
        return existing;
    }

    private void merge(BrandCreateEditRequestDto requestDto, Brand existing) {
        existing.setName(requestDto.getName());
    }
}