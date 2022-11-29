package com.dmdev.mapper.brand;

import com.dmdev.domain.dto.brand.request.BrandCreateEditRequestDto;
import com.dmdev.domain.entity.Brand;
import com.dmdev.mapper.UpdateMapper;
import org.springframework.stereotype.Component;

@Component
public class BrandUpdateMapper implements UpdateMapper<BrandCreateEditRequestDto, Brand> {

    @Override
    public Brand mapToEntity(BrandCreateEditRequestDto requestDto, Brand existing) {
        merge(requestDto, existing);
        return existing;
    }

    @Override
    public void merge(BrandCreateEditRequestDto requestDto, Brand existing) {

        existing.setName(requestDto.getName());
    }
}