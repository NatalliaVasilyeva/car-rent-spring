package com.dmdev.mapper.brand;

import com.dmdev.domain.dto.brand.request.BrandCreateEditRequestDto;
import com.dmdev.domain.entity.Brand;
import com.dmdev.mapper.CreateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandCreateEditMapper implements CreateMapper<BrandCreateEditRequestDto, Brand> {

    @Override
    public Brand mapToEntity(BrandCreateEditRequestDto requestDto) {
        return Brand.builder()
                .name(requestDto.getName())
                .build();
    }
}